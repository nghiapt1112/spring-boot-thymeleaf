package com.lyna.web.user;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.utils.JsonUtils;
import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.logicstics.LogisticRequestPage;
import com.lyna.web.domain.logicstics.LogisticResponsePage;
import com.lyna.web.domain.logicstics.StoreRequestPage;
import com.lyna.web.domain.logicstics.StoreResponsePage;
import com.lyna.web.domain.logicstics.service.LogisticService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogisticRepositoryTest extends LynaApplicationTests {

    @Autowired
    private LogisticService logisticService;

    @Test
    public void findOrderPage() {
        int tenantId = 1;
        RequestPage requestPage = new StoreRequestPage();
        requestPage.setTenantId(tenantId);
        StoreResponsePage response = this.logisticService.findOrdersAndPaging(requestPage);

        try {
            Files.write(Paths.get("/mnt/ng-data/PROJECT/lyna/lyna/nghia.log"), JsonUtils.toBytes(response.rawResults));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(response);
    }


    @Test
    public void findLogisticPage() {
        int tenantId = 1;
        RequestPage requestPage = new LogisticRequestPage();
        requestPage.setTenantId(tenantId);
        LogisticResponsePage response = this.logisticService.findLogisticsAndPaging(requestPage);

        try {
            Files.write(Paths.get("/mnt/ng-data/PROJECT/lyna/lyna/nghia.log"), JsonUtils.toBytes(response.rawResults));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(response);
    }

}
