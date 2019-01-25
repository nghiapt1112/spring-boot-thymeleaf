package com.lyna.web.ai;

import com.lyna.commons.utils.HttpUtils;
import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.AI.AIDataAggregate;
import com.lyna.web.domain.AI.AIService;
import com.lyna.web.domain.AI.AIServiceImpl;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.infrastructure.property.AIProperty;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AIServiceTest extends LynaApplicationTests {

    List<String> orderIds;
    User currentUser;
    @Autowired
    private AIServiceImpl aiService;
    //    private AIService aiService;
    @Autowired
    private AIProperty aiProperty;
    @Autowired
    private LogisticRepository logisticRepository;

    @Before
    public void init() {
//        Mockito.when(HttpUtils.post(aiProperty.getUrl(), aiProperty.getHeaders(), null, String.class))
//                .thenReturn("error");

        this.orderIds = Arrays.asList("5671635f-0b3a-49e4-bd5f-45db4b35c047", "5c36d3dd-bb46-4380-9ce4-093c0677d5c6", "8b460a2d-166f-41ac-a0a3-5d3cc7fe3924", "cd47925e-a241-4f04-9c9e-eeafc49cd3fd", "e60a396b-d44b-4c61-8422-675e05038fbb", "f9e32e4f-185b-4703-8cd0-03b09cec14ef");
        currentUser = new User();
        currentUser.setTenantId(1);

    }

    @Test
    public void getData() {

        User currentUser = new User();
        currentUser.setTenantId(1);
        this.aiService.calculateLogisticsWithAI(currentUser, orderIds);


        String res = HttpUtils.post(aiProperty.getUrl(), aiProperty.getHeaders(), null, String.class);

        Assert.assertEquals("error", res);
    }

    @Test
    public void findLogistics() {
        Set<String> uniqueExistedOrderIds = new HashSet<>();
        for (String orderId : orderIds) {
            if (uniqueExistedOrderIds.contains(orderId)) {
                System.out.println("ton tai");
                printAsJson("ton tai");
            }
        }
        System.out.println();
    }

    @Test
    public void updateFromAIResponse() {
        AIDataAggregate response = readFile("ai/ai-response.json", AIDataAggregate.class);

        aiService.updateDataToDB(currentUser, response.getResultDatas());
    }

    @Test
    public void requestError() {
        // doc data test tu file JSON
        AIDataAggregate requestObject = readFile("ai/ai-request.json", AIDataAggregate.class);
        AIDataAggregate response = HttpUtils.post(aiProperty.getUrl(), aiProperty.getHeaders(), requestObject, AIDataAggregate.class);
        if (response.getCode() == 1) {
            response.setMessage("解析に失敗しました。");
        }
        printAsJson(response);
    }

    @After
    public void clean() {
        this.orderIds = null;
        this.currentUser = null;
    }
}
