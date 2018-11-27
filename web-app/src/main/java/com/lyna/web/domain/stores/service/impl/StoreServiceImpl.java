package com.lyna.web.domain.stores.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.exception.StoreException;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.exception.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class StoreServiceImpl extends BaseService implements StoreService {

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private StoreRepository storeRepository;

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



    @Transactional
    public void save(Store store) {
         if(Objects.isNull(store)){
            throw new StoreException(toInteger("err.user.duplicateUser.code"), toStr("err.user.duplicateUser.msg"));
        }
        try {
            storeRepository.save(store);
        }catch (Exception ex){
            throw new StoreException(toInteger("err.store.saveFailed.code"), toStr("err.store.saveFailed.msg"));
        }
    }

    @Override
    public Store findOneByStoreId(String storeId) {
        return storeRepository.findOneByStoreId(storeId);
    }

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }
}
