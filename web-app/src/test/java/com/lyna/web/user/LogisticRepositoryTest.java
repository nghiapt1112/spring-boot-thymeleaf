package com.lyna.web.user;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.logicstics.StoreRequestPage;
import com.lyna.web.domain.logicstics.StoreResponsePage;
import com.lyna.web.domain.logicstics.service.LogisticService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LogisticRepositoryTest extends LynaApplicationTests {

    @Autowired
    private LogisticService logisticService;

    @Test
    public void findOrderPage() {
        int tenantId = 1;
        RequestPage requestPage = new StoreRequestPage();
        StoreResponsePage response = this.logisticService.findOrdersAndPaging(requestPage);

//        System.out.println(response);
    }

    @Test
    public void findLogisticPage() {

    }


}
