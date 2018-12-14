package com.lyna.web.user;

import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.logicstics.LogisticRequestPage;
import com.lyna.web.domain.logicstics.LogisticView;
import com.lyna.web.domain.logicstics.repository.LogisticViewRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.lyna.web.domain.logicstics.LogisticRequestPage.END_DATE;
import static com.lyna.web.domain.logicstics.LogisticRequestPage.POST_NAME;
import static com.lyna.web.domain.logicstics.LogisticRequestPage.START_DATE;
import static com.lyna.web.infrastructure.utils.DateTimeUtils.convertStringToDate;

public class LogisticViewTest extends LynaApplicationTests {

    @Autowired
    private LogisticViewRepository logisticViewRepository;

    @Test
    public void findAllInTenant() {
        LogisticRequestPage logisticRequestPage = new LogisticRequestPage();
        logisticRequestPage
                .withTenantId(1)
                .addSearchField(START_DATE, convertStringToDate("2018-11-25 00:00:00"))
                .addSearchField(END_DATE, convertStringToDate("2018-11-27 00:00:00"))
                .addSearchField(POST_NAME, "便１");

        List<LogisticView> logisticData = logisticViewRepository.findLogistics(1, logisticRequestPage);
        Assert.assertNotNull(logisticData);
    }

}
