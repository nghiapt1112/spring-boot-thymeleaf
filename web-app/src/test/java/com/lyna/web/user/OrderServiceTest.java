package com.lyna.web.user;

import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.order.OrderView;
import com.lyna.web.domain.order.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderServiceTest extends LynaApplicationTests {

    @Autowired
    private OrderService orderService;


    @Test
    public void findOrderViews() {
        List<OrderView> orderViews = this.orderService.findOrderViews(1);

        Assert.assertNotNull(orderViews);
        printAsJson(orderViews);
    }
}
