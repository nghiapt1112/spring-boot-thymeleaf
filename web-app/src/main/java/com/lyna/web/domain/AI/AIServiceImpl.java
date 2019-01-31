package com.lyna.web.domain.AI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.commons.utils.Constants;
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
import com.lyna.web.domain.trainningData.Training;
import com.lyna.web.domain.trainningData.service.TrainingService;
import com.lyna.web.domain.user.User;
import com.lyna.web.infrastructure.property.AIProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl extends BaseService implements AIService {
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

    @Autowired
    private TrainingService trainingService;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    @Transactional
    public int calculateLogisticsWithAI(User currentUser, Collection<String> csvOrderIds) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderIds(currentUser.getTenantId(), csvOrderIds);
        if (CollectionUtils.isEmpty(orderDetails)) {
            return Constants.AI_STATUS.EMPTY;
        }
        Map<String, Map<String, BigDecimal>> amountByOrderId = new HashMap<>();
        for (OrderDetail orderDetail : orderDetails) {
            Map<String, BigDecimal> v = amountByOrderId.get(orderDetail.getOrderId());
            if (Objects.isNull(v)) {
                v = new HashMap<>();
            }
            v.put(orderDetail.getProductId(), orderDetail.getAmount());
            amountByOrderId.put(orderDetail.getOrderId(), v);
        }

        int tenantId = currentUser.getTenantId();

        List<Product> productInTenants = productRepository.findByTenantId(tenantId);
        List<Training> trainingList = trainingService.findAllByTenantId(tenantId);
        List<Package> packages = packageRepository.findByTenantId(tenantId);

        Map<String, List<Integer>> unknowAIDatas = new HashMap<>();
        List<TrainingData> trainingAIDatas = new ArrayList<>();
        amountByOrderId.forEach((orderId, amountByProductId) ->
                unknowAIDatas.put(orderId, this.fillProductAmountToEachOrder(productInTenants, amountByProductId))
        );

        if (trainingList != null && trainingList.size() > 0) {
            trainingList.forEach(training -> {
                TrainingData trainingData = new TrainingData();
                trainingData.setInputItemNums(this.fileProductAmountToEachOrder(training.getInputItemNums(), productInTenants));
                trainingData.setOutputItemNums(this.filePackageAmountToEachOrder(training.getOutputItemNums(), packages));
                trainingAIDatas.add(trainingData);
            });
        } else {
            TrainingData trainingData = new TrainingData();
            trainingData.setInputItemNums(productInTenants.stream().parallel().map(product -> 0).collect(Collectors.toList()));
            trainingData.setOutputItemNums(packages.stream().parallel().map(p -> 0).collect(Collectors.toList()));
            trainingAIDatas.add(trainingData);
        }

        AIDataAggregate aggregateRequest = new AIDataAggregate(unknowAIDatas, trainingAIDatas);
        try {
            AIDataAggregate response = HttpUtils.post(aiProperty.getUrl(), aiProperty.getHeaders(), aggregateRequest, AIDataAggregate.class);
            if (response.getCode() == 1) {
                return response.getCode();
            }
            updateDataToDB(currentUser, response.getResultDatas());
        } catch (Exception e) {
            return Constants.AI_STATUS.ERROR;
        }
        return Constants.AI_STATUS.SUCCESS;
    }

    @Override
    @Transactional
    public void updateDataToDB(User currentUser, List<UnknownData> resultDatas) {
        Set<String> responseAIOrderIds = resultDatas.stream().map(UnknownData::getOrderId).collect(Collectors.toSet());
        List<Logistics> existedLogst = this.logisticRepository.findByOrderIds(currentUser.getTenantId(), responseAIOrderIds);

        List<Logistics> newLogst = this.createLogistics(currentUser, responseAIOrderIds, existedLogst);

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
        if (CollectionUtils.isEmpty(existedLogst)) {
            return;
        }
        List<LogiticsDetail> existedLogsDetails = new ArrayList<>();

        this.func(dataByOrderId, existedLogst, (el, amountByPackageId) -> {
            if (CollectionUtils.isEmpty(el.getLogiticsDetails())) {
                return;
            }
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

    private List<Integer> fileProductAmountToEachOrder(String inputItemNums, List<Product> productInTenants) {
        TypeReference<HashMap<String, BigDecimal>> typeRef
                = new TypeReference<HashMap<String, BigDecimal>>() {
        };
        try {
            Map<String, BigDecimal> mapInputItem = mapper.readValue(inputItemNums, typeRef);
            return fillProductAmountToEachOrder(productInTenants, mapInputItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<Integer> filePackageAmountToEachOrder(String packageOuput, List<Package> packages) {
        TypeReference<Map<String, Integer>> typeRef
                = new TypeReference<Map<String, Integer>>() {
        };
        try {
            Map<String, Integer> packageOutput = mapper.readValue(packageOuput, typeRef);
            return packages
                    .stream()
                    .parallel()
                    .map(p -> {
                        Integer amount = packageOutput.get(p.getPackageId());
                        if (Objects.isNull(amount)) {
                            return 0;
                        }
                        return amount.intValue();
                    })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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


    private List<Integer> fillProductAmountToEachOrder(List<Product> productInTenants, Map<String, BigDecimal> amountByProductId) {
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
