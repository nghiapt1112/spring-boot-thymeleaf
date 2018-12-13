package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.ResponsePage;
import com.lyna.web.domain.stores.StoreDTO;
import com.lyna.web.domain.view.StoreAggregate;

import java.util.List;
import java.util.stream.Collectors;

public class StoreResponsePage extends ResponsePage<StoreDTO, StoreAggregate> {
    @Override
    protected List<StoreAggregate> parseResult(List<StoreDTO> rawResults) {
        return rawResults.stream()
                .map(StoreAggregate::fromDTO)
                .collect(Collectors.toList());
    }

}
