package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.ResponsePage;
import com.lyna.web.domain.view.LogisticAggregate;

import java.util.List;

public class LogisticResponsePage extends ResponsePage<Logistics, LogisticAggregate> {
    @Override
    protected List<LogisticAggregate> parseResult(List<Logistics> rawResults) {
        return null;
    }

    @Override
    public List<LogisticAggregate> getResults() {
        return null;
    }
}
