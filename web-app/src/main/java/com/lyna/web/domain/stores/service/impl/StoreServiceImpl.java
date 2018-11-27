package com.lyna.web.domain.stores.service.impl;

import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.stores.repository.StoreRepositoryJpa;
import com.lyna.web.domain.stores.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreRepositoryJpa storeRepositoryJpa;

    @Autowired
    private PostCourseRepository postCourseRepository;


    public List<Store> findAll(int tenantId) {
        //TODO: =>nghia.pt replace with storeRepository.findByTenant(...)
        return this.storeRepository.findAll(tenantId);
    }

    @Override
    public List<Store> getStoreList(int principal) {
        return storeRepository.getAll(principal);
    }


    @Override
    @Transactional
    public void save(Store store) {
        try {
            storeRepository.save(store);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
    }

    @Override
    public Store findOneByStoreId(String storeId) {
        return storeRepositoryJpa.findOneByStoreId(storeId);
    }

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }
}
