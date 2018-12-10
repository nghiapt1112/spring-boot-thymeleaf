package com.lyna.web.user;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.utils.JsonUtils;
import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.logicstics.LogisticRequestPage;
import com.lyna.web.domain.logicstics.LogisticResponsePage;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import com.lyna.web.domain.logicstics.StoreRequestPage;
import com.lyna.web.domain.logicstics.StoreResponsePage;
import com.lyna.web.domain.logicstics.repository.LogisticDetailRepository;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.domain.logicstics.service.LogisticService;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LogisticRepositoryTest extends LynaApplicationTests {

    @Autowired
    private LogisticService logisticService;


    @Autowired
    private LogisticRepository logisticRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private LogisticDetailRepository logisticDetailRepository;

    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;

    @Test
    public void findOrderPage() {
        int tenantId = 1;
        RequestPage requestPage = new StoreRequestPage();
        requestPage.setTenantId(tenantId);
        StoreResponsePage response = this.logisticService.findOrdersAndPaging(requestPage);

        try {
            Files.write(Paths.get("/home/nghiapt/Desktop/order-result.log"), JsonUtils.toBytes(response.getResults()));
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
            Files.write(Paths.get("/home/nghiapt/Desktop/logistic-result.log"), JsonUtils.toBytes(response.getResults()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(response);
    }


    @Test
    public void findPackage() {
        List<Package> allLogistics = this.packageRepository.findAll();
        int limitData = 200;
        Set<String> packageIds = allLogistics.stream()
                .limit(limitData)
                .map(el -> el.getPakageId())
                .collect(Collectors.toSet());

        Collection<Package> data = this.packageRepository.findByIds(1, packageIds);
        Assert.assertEquals(limitData, data.size());

//        data.stream()
//                .collect(Collectors.toMap(Package::getPakageId))
    }


    @Test
    public void findLogisticDetails() throws ExecutionException, InterruptedException {
        CompletableFuture<List<LogiticsDetail>> logDetail = CompletableFuture.supplyAsync(() -> this.logisticDetailRepository.findByLogisticIds(1, Arrays.asList("667f191e810c19119de913eg")));
        List<LogiticsDetail> datax = logDetail.get();
        System.out.println("\n\n data return" + Objects.isNull(logDetail));
        List<LogiticsDetail> data = this.logisticDetailRepository.findByLogisticIds(1, Arrays.asList("667f191e810c19119de913eg"));
        System.out.println(data.size());
        Map<String, List<LogiticsDetail>> mapData = data.stream()
                .collect(Collectors.groupingBy(LogiticsDetail::getLogisticsId));
        System.out.println();
    }

    @Test
    public void findLogisticsByOrderIds() {
        List<LogiticsDetail> logDetails = this.logisticDetailRepository.findByOrderIds(1, Arrays.asList("667f191e810c22110de100aaa", "667f191e810c22110de101aaa"));
        printAsJson(logDetails);
    }

    @Test
    public void findDeliveriesByOrderIds() {
        List<DeliveryDetail> logDetails = this.deliveryDetailRepository.findByOrderIds(1, Arrays.asList("667f191e810c22110de100aaa", "667f191e810c22110de101aaa"));
        printAsJson(logDetails);
    }

}
