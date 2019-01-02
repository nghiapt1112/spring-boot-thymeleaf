package com.lyna.web.domain.AI;

import com.lyna.commons.infrustructure.service.BaseService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

    //    @Async
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

//        AIDataAggregate response = post(aiProperty.getUrl(), aiProperty.getHeaders(), new AIDataAggregate(unknowAIDatas), AIDataAggregate.class);
//
//        this.updateDatToDB(currentUser, orderIds, response.getResultDatas());
        System.out.println();
    }

    @Transactional
    public void updateDatToDB(User currentUser, List<UnknownData> resultDatas) {
        Set<String> orderIds = resultDatas.stream().map(el -> el.getOrderId()).collect(Collectors.toSet());
        List<Logistics> existedLogs = this.logisticRepository.findByOrderIds(currentUser.getTenantId(), orderIds);

        Set<String> uniqueExistedOrderIds = new HashSet<>();
        Set<String> uniqueExistedLogisticIds = new HashSet<>();
        for (Logistics el : existedLogs) {
            uniqueExistedOrderIds.add(el.getOrderId());
            uniqueExistedLogisticIds.add(el.getLogisticsId());
        }

        List<Logistics> newLogst = new ArrayList<>();
        this.createLogistics(currentUser, orderIds, uniqueExistedOrderIds, newLogst);



        //  orderId <-> {packageId-amount}
        Map<String, Map<String, Integer>> dataByOrderId = this.getPackageAmountByOrderId(currentUser.getTenantId(), resultDatas);

        //  TODO: update t_logsitc_detail: update packageId and amount
        List<LogiticsDetail> existedLogisticDetails = this.logisticDetailRepository.findByLogisticIds(currentUser.getTenantId(), uniqueExistedLogisticIds);
        if (uniqueExistedLogisticIds.size() != existedLogisticDetails.size()) {
            throw new AIException(toInteger("err.ai.dataInvalid.code"), toStr("err.ai.dataInvalid.msg"));
        }

        for (Logistics existedLog : existedLogs) {

            Map<String, Integer> amountByPackageId = dataByOrderId.get(existedLog.getOrderId());
            for (LogiticsDetail logiticsDetail : existedLog.getLogiticsDetails()) {
                Integer existedDetail = amountByPackageId.get(logiticsDetail.getPackageId());
            }
            // delete theo orderIds.
            // insert theo orderIds. nhung field la update.
        }

        //  TODO: create t_logsitc_detail
        this.createLogisticDetails(currentUser, dataByOrderId, newLogst);

    }

    /**
     * create logistic_detail
     */
    private void createLogisticDetails(User currentUser, Map<String, Map<String, Integer>> dataByOrderId, List<Logistics> newLogst) {
        List<LogiticsDetail> newLogDetails = new ArrayList<>();
        for (Logistics el : newLogst) {
            //  packageId <-> amount
            Map<String, Integer> amountByPackageId = dataByOrderId.get(el.getOrderId());
            if (Objects.isNull(amountByPackageId)) {
                throw new AIException(toInteger("err.ai.dataInvalid.code"), toStr("err.ai.dataInvalid.msg"));
            }
            amountByPackageId.forEach((packageId, amount) ->
                    newLogDetails.add(new LogiticsDetail(currentUser, amount, packageId, el.getLogisticsId()))
            );
        }
        this.logisticDetailRepository.saveAll(newLogDetails);
    }

    /**
     * create t_logsitc: repository.save(newLogst)
     */
    private void createLogistics(User currentUser, Set<String> orderIds, Set<String> uniqueExistedOrderIds, List<Logistics> newLogst) {
        for (String orderId : orderIds) {
            if (!uniqueExistedOrderIds.contains(orderId)) {
                newLogst.add(new Logistics(currentUser, orderId));
            } else {
                // nhung cai da existed thi khong can update vi bang t_logistics thi cha co gi can de update ca.
                // TODO: sau khi nghi lai thi phai update update_user, update_date
            }
        }
        this.logisticRepository.saveAll(newLogst);
    }

    /**
     *  create a map:  orderId <-> {packageId-amount}
     *
     *  data return will contain of both existed logistic_detail and new logistic_detail.
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
