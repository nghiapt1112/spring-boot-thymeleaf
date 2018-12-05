package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.logicstics.Logistics;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class LogisticRepositoryImpl extends BaseRepository<Logistics, String> implements LogisticRepository {

    public LogisticRepositoryImpl(EntityManager em) {
        super(Logistics.class, em);
    }

}
