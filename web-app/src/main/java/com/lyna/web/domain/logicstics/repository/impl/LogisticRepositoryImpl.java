package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.web.domain.logicstics.Logistics;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class LogisticRepositoryImpl extends BaseRepository<Logistics, String> implements LogisticRepository, PagingRepository {

    public LogisticRepositoryImpl(EntityManager em) {
        super(Logistics.class, em);
    }

}
