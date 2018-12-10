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
        return rawResults.stream()
                .parallel()
                .map(LogisticAggregate::fromLogisticDTO)
                .collect(Collectors.toList());
    }

}
