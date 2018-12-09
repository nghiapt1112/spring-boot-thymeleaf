package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.ResponsePage;
import com.lyna.web.domain.view.LogisticAggregate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogisticResponsePage extends ResponsePage<LogisticDTO, LogisticAggregate> {
    @Override
    protected List<LogisticAggregate> parseResult(List<LogisticDTO> rawResults) {

        Map<String, Object> packageById = new HashMap<>();

        rawResults.stream()
                .parallel()
//                .map(LogisticAggregate::fromLogisticDTO)
                .collect(Collectors.toMap(LogisticDTO::getStoreId, LogisticDTO::getAmount, (v1,v2) -> v1));

        return rawResults.stream()
                .parallel()
                .map(LogisticAggregate::fromLogisticDTO)
                .collect(Collectors.toList());
    }

}
