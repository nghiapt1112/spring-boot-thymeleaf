package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.ResponsePage;
import com.lyna.commons.utils.JsonUtils;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.view.StoreAggregate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class StoreResponsePage extends ResponsePage<Store, StoreAggregate> {
    @Override
    protected List<StoreAggregate> parseResult(List<Store> rawResults) {
//        System.out.println(JsonUtils.toJson(rawResults));
        try {
            Files.write(Paths.get("/mnt/ng-data/PROJECT/lyna/lyna/nghia.log"), JsonUtils.toBytes(rawResults));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StoreAggregate aggregate = new StoreAggregate();
        return rawResults.stream()
                .map(el -> StoreAggregate.fromEntity(el))
                .collect(Collectors.toList());
    }

}
