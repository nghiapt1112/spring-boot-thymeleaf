package com.lyna.web.user;

import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.order.OrderAggregate;
import com.lyna.web.domain.order.OrderRequestPage;
import com.lyna.web.domain.order.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.lyna.commons.utils.DateTimeUtils.convertStringToDate;
import static com.lyna.web.domain.logicstics.LogisticRequestPage.END_DATE;
import static com.lyna.web.domain.logicstics.LogisticRequestPage.POST_NAME;
import static com.lyna.web.domain.logicstics.LogisticRequestPage.START_DATE;

public class OrderServiceTest extends LynaApplicationTests {

    @Autowired
    private OrderService orderService;


    @Test
    public void findOrderViews() {
        OrderRequestPage orderQueryBuilder = new OrderRequestPage();
        orderQueryBuilder
                .withTenantId(1)
                .addSearchField(START_DATE, convertStringToDate("2018-11-25 00:00:00"))
                .addSearchField(END_DATE, convertStringToDate("2018-11-27 00:00:00"))
                .addSearchField(POST_NAME, "便１")
                .build();

        List<OrderAggregate> orderViews = this.orderService.findOrderViews(1, orderQueryBuilder);

        Assert.assertNotNull(orderViews);
        printAsJson(orderViews);
    }
}
