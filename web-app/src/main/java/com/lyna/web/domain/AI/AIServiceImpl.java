package com.lyna.web.domain.AI;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.logicstics.Logistics;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.infrastructure.property.AIProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lyna.commons.utils.HttpUtils.post;

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

    @Async
    public void calculateLogisticsWithAI(User currentUser, Set<String> orderIds) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderIds(currentUser.getTenantId(), orderIds);
        if (CollectionUtils.isEmpty(orderDetails)) {
            return;
        }
        orderIds.clear();
        // Map: orderId -> Map{productId - amount}
        Map<String, Map<String, BigDecimal>> productAmountByProductId = new HashMap<>();

        for (OrderDetail orderDetail : orderDetails) {
            Map<String, BigDecimal> v = productAmountByProductId.get(orderDetail.getOrderId());
            if (MapUtils.isEmpty(v)) {
                v = new HashMap<>();
            }
            v.put(orderDetail.getProductId(), orderDetail.getAmount());
            productAmountByProductId.put(orderDetail.getOrderId(), v);
            orderIds.add(orderDetail.getOrderId());
        }

        List<Product> productInTenants = productRepository.findByTenantId(currentUser.getTenantId());

        Map<String, List<Integer>> unknowDatasAI = new HashMap<>();
        productAmountByProductId.forEach((orderId, amountByProductId) ->
                unknowDatasAI.put(orderId, this.fillPackageAmountToEachOrder(productInTenants, amountByProductId))
        );

        AIDataAggregate response = post(aiProperty.getUrl(), aiProperty.getHeaders(), new AIDataAggregate(unknowDatasAI), AIDataAggregate.class);

        this.updateDatToDB(currentUser, orderIds, response.getResultDatas(), productInTenants);
    }

    private void updateDatToDB(User currentUser, Set<String> orderIds, List<UnknownData> resultDatas, List<Product> productInTenants) {
        List<Logistics> ls = this.logisticRepository.findByOrderIds(currentUser.getTenantId(), orderIds);
        Set<String> uniqueExistedOrderIds = ls.stream().map(Logistics::getOrderId).collect(Collectors.toSet());

//        TODO: create t_logsitc, t_logsitc_detail
//        TODO: update t_logsitc, t_logsitc_detail
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
