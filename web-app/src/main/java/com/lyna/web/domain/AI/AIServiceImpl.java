package com.lyna.web.domain.AI;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.commons.utils.HttpUtils;
import com.lyna.web.domain.logicstics.Logistics;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import com.lyna.web.domain.logicstics.repository.LogisticDetailRepository;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.infrastructure.property.AIProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl extends BaseService {
    @Autowired
    private AIProperty aiProperty;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private LogisticRepository logisticRepository;

    @Autowired
    private LogisticDetailRepository logisticDetailRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Async
    public void calculateLogisticsWithAI(User currentUser, Collection<String> csvOrderIds) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderIds(currentUser.getTenantId(), csvOrderIds);
        if (CollectionUtils.isEmpty(orderDetails)) {
            return;
        }
        Set<String> orderIds = new HashSet<>();
        // Map: orderId -> Map{productId - amount}
        Map<String, Map<String, BigDecimal>> amountByOrderId = new HashMap<>();

        for (OrderDetail orderDetail : orderDetails) {
            Map<String, BigDecimal> v = amountByOrderId.get(orderDetail.getOrderId());
            if (Objects.isNull(v)) {
                v = new HashMap<>();
            }
            v.put(orderDetail.getProductId(), orderDetail.getAmount());
            amountByOrderId.put(orderDetail.getOrderId(), v);
            orderIds.add(orderDetail.getOrderId());
        }

        List<Product> productInTenants = productRepository.findByTenantId(currentUser.getTenantId());

        Map<String, List<Integer>> unknowAIDatas = new HashMap<>();
        amountByOrderId.forEach((orderId, amountByProductId) ->
                unknowAIDatas.put(orderId, this.fillPackageAmountToEachOrder(productInTenants, amountByProductId))
        );

        AIDataAggregate response = HttpUtils.post(aiProperty.getUrl(), aiProperty.getHeaders(), new AIDataAggregate(unknowAIDatas), AIDataAggregate.class);

        this.updateDataToDB(currentUser, response.getResultDatas());
    }

    @Transactional
    public void updateDataToDB(User currentUser, List<UnknownData> resultDatas) {
        Set<String> responseAIOrderIds = resultDatas.stream().map(el -> el.getOrderId()).collect(Collectors.toSet());
        List<Logistics> existedLogst = this.logisticRepository.findByOrderIds(currentUser.getTenantId(), responseAIOrderIds);

        List<Logistics> newLogst = this.createLogistics(currentUser, responseAIOrderIds, existedLogst);

        //  orderId <-> {packageId-amount}
        Map<String, Map<String, Integer>> dataByOrderId = this.getPackageAmountByOrderId(currentUser.getTenantId(), resultDatas);

        this.updateLogisticDetails(currentUser, dataByOrderId, existedLogst);

        this.createLogisticDetails(currentUser, dataByOrderId, newLogst);
    }

    private void func(Map<String, Map<String, Integer>> dataByOrderId, List<Logistics> logsts, BiConsumer<Logistics, Map<String, Integer>> c) {
        for (Logistics el : logsts) {
            Map<String, Integer> amountByPackageId = dataByOrderId.get(el.getOrderId());
            c.accept(el, amountByPackageId);
        }
    }

    private void updateLogisticDetails(User currentUser, Map<String, Map<String, Integer>> dataByOrderId, List<Logistics> existedLogst) {
        List<LogiticsDetail> existedLogsDetails = new ArrayList<>();

        this.func(dataByOrderId, existedLogst, (el, amountByPackageId) -> {
            for (LogiticsDetail logiticsDetail : el.getLogiticsDetails()) {
                logiticsDetail.updateInfo(currentUser, amountByPackageId.get(logiticsDetail.getPackageId()));
            }
            existedLogsDetails.addAll(el.getLogiticsDetails());
        });

        this.logisticDetailRepository.saveAll(existedLogsDetails);
    }

    /**
     * create logistic_detail
     */
    private void createLogisticDetails(User currentUser, Map<String, Map<String, Integer>> dataByOrderId, List<Logistics> newLogst) {
        List<LogiticsDetail> newLogsDetails = new ArrayList<>();

        this.func(dataByOrderId, newLogst, (el, amountByPackageId) -> {
            if (Objects.isNull(amountByPackageId)) {
                throw new AIException(toInteger("err.ai.dataInvalid.code"), toStr("err.ai.dataInvalid.msg"));
            }
            amountByPackageId.forEach((packageId, amount) ->
                    newLogsDetails.add(new LogiticsDetail(currentUser, amount, packageId, el.getLogisticsId()))
            );
        });
        this.logisticDetailRepository.saveAll(newLogsDetails);
    }

    /**
     * create t_logsitc: repository.save(newLogst)
     */
    private List<Logistics> createLogistics(User currentUser, Set<String> responseAIOrderIds, List<Logistics> existedLogs) {
        Set<String> existedOrderIds = existedLogs.stream().map(Logistics::getOrderId).collect(Collectors.toSet());

        List<Logistics> newLogst = new ArrayList<>();
        for (String orderId : responseAIOrderIds) {
            if (!existedOrderIds.contains(orderId)) {
                newLogst.add(new Logistics(currentUser, orderId));
            }
        }
        List<Logistics> res = this.logisticRepository.saveAll(newLogst);
        newLogst.clear();
        return res;
    }

    /**
     * create a map:  orderId <-> {packageId-amount}
     * <p>
     * data return will contain of both existed logistic_detail and new logistic_detail.
     */
    private Map<String, Map<String, Integer>> getPackageAmountByOrderId(int tenantId, List<UnknownData> resultDatas) {
        Map<String, Map<String, Integer>> res = new HashMap<>();
        List<Package> pkgsInTenant = this.packageRepository.findByTenantId(tenantId);

        for (UnknownData el : resultDatas) {
            if (el.getOutputItemNums().size() != pkgsInTenant.size()) {
                throw new AIException(toInteger("err.ai.dataInvalid.code"), toStr("err.ai.dataInvalid.msg"));
            }
            res.put(el.getOrderId(), el.toMap(pkgsInTenant));
        }
        return res;
    }

    private List<Integer> fillPackageAmountToEachOrder(List<Product> productInTenants, Map<String, BigDecimal> amountByProductId) {
        return productInTenants
                .stream()
                .parallel()
                .map(product -> {
                    BigDecimal amount = amountByProductId.get(product.getProductId());
                    if (Objects.isNull(amount)) {
                        return 0;
                    }
                    return amount.intValue();
                })
                .collect(Collectors.toList());
    }

}
