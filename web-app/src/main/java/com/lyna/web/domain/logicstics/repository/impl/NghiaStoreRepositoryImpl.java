package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.logicstics.repository.NghiaStoreRepository;
import com.lyna.web.domain.stores.Store;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class NghiaStoreRepositoryImpl extends BaseRepository<Store, String> implements NghiaStoreRepository {
    public NghiaStoreRepositoryImpl(EntityManager em) {
        super(Store.class, em);
    }
}
