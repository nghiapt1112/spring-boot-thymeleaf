package com.lyna.web.infrastructure.services;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.commons.utils.HttpUtils;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl extends BaseService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Value("${lyna.ai.url}")
    private String AI_URL;

    public void callTraining(int tenantId, Set<String> orderIds) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderIds(tenantId, orderIds);
        if (CollectionUtils.isEmpty(orderDetails)) {
            return;
        }

        Map<String, List<OrderDetail>> orderDetailsByOrderId = orderDetails.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId, Collectors.mapping(el -> el, Collectors.toList())));

        // Map: orderId -> Map{productId - amount}
        Map<String, Map<String, BigDecimal>> productAmountByProductId = new HashMap<>();

        orderDetailsByOrderId.forEach((orderId, v) ->
            productAmountByProductId.put(orderId, v.stream()
                    .collect(Collectors.toMap(OrderDetail::getProductId, OrderDetail::getAmount, (o1, o2) -> o1)))
        );

        List<Product> productInTenants = productRepository.findByTenantId(tenantId);

        Map<String, List<Integer>> unknowDatasAI = new HashMap<>();
        productAmountByProductId.forEach((orderId, amountByProductId) -> {
            List<Integer> parsedVal = this.fillPackageAmountToEachOrder(productInTenants, amountByProductId);
            unknowDatasAI.put(orderId, parsedVal);

        });


        AIData data = new AIData();
        data.parse(unknowDatasAI);

        Object response = HttpUtils.httpsPost(AI_URL, data, null);

        // TODO: parse data save to logistic_detail, logistic
    }

    private List<Integer> fillPackageAmountToEachOrder(List<Product> productInTenants, Map<String, BigDecimal> amountByProductId) {
        return productInTenants
                        .stream()
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
