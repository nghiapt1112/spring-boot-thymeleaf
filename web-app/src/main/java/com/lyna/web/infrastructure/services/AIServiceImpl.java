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

    public void callTraining(Set<String> orderIds) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderIds(1, orderIds);
        if (CollectionUtils.isEmpty(orderDetails)) {
            return;
        }

        Map<String, List<OrderDetail>> orderDetailsByOrderId = orderDetails.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId, Collectors.mapping(el -> el, Collectors.toList())));

        List<Product> productInTenants = productRepository.findByTenantId(1);


        // Map: orderId -> Map{productId - amount}
        Map<String, Map<String, BigDecimal>> productAmountByProductId = new HashMap<>();

        orderDetailsByOrderId.forEach((orderId, v) ->
            productAmountByProductId.put(orderId, v.stream()
                    .collect(Collectors.toMap(OrderDetail::getProductId, OrderDetail::getAmount, (o1, o2) -> o1)))
        );


        List<List<Integer>> dataAI = productAmountByProductId
                .entrySet()
                .stream()
                .map(entry -> productInTenants
                        .stream()
                        .map(product -> {
                            BigDecimal amount = entry.getValue().get(product.getProductId());
                            if (Objects.isNull(amount)) {
                                return 0;
                            }
                            return amount.intValue();
                        })
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());

        HttpUtils.httpsPost(AI_URL, dataAI, null);
    }

}
