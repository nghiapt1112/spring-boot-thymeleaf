package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.web.domain.logicstics.repository.NghiaStoreRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.infrastructure.repository.BaseRepository;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class NghiaStoreRepositoryImpl extends BaseRepository<Store, String> implements NghiaStoreRepository, PagingRepository {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public NghiaStoreRepositoryImpl(EntityManager em) {
        super(Store.class, em);
    }
}
