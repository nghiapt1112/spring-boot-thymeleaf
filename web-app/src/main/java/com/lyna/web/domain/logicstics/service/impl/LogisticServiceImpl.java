package com.lyna.web.domain.logicstics.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.logicstics.service.LogisticService;
import com.lyna.web.domain.view.LogisticAggregate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogisticServiceImpl extends BaseService implements LogisticService {

    @Override
    public List<LogisticAggregate> findLogistic() {
        return null;
    }
}
