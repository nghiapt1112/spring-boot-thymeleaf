package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.ResponsePage;
import com.lyna.web.domain.view.LogisticAggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class LogisticResponsePage extends ResponsePage<Logistics, LogisticAggregate> {
    @Override
    protected List<LogisticAggregate> parseResult(List<Logistics> rawResults) {
        List<LogisticAggregate> aggregates = new ArrayList<>();

        IntStream.range(1, 100)
                .parallel()
                .forEach(el -> {
                    aggregates.add(new LogisticAggregate().initTestData());
                });
        return aggregates;
    }

//    @Override
//    public List<LogisticAggregate> getResults() {
//        return null;
//    }
}
