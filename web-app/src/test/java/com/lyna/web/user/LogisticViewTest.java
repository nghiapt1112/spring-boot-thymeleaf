package com.lyna.web.user;

import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.logicstics.LogisticView;
import com.lyna.web.domain.logicstics.repository.LogisticViewRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LogisticViewTest extends LynaApplicationTests {

    @Autowired
    private LogisticViewRepository logisticViewRepository;

    @Test
    public void findAllInTenant() {
        List<LogisticView> lgsInTenant = this.logisticViewRepository.findLogistics(1);
        Assert.assertNotNull(lgsInTenant);
        Assert.assertNotEquals(0, lgsInTenant.size());
        printAsJson(lgsInTenant);
    }
}
