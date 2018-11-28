package com.lyna.web.domain.stores.service;

import com.lyna.web.domain.stores.Store;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface StoreService {
    List<Store> findAll(int tenantId);

    List<Store> getStoreList(int tenantId);

    void createStore(Store store, UsernamePasswordAuthenticationToken principal);

    void updateStore(Store store, UsernamePasswordAuthenticationToken principal);


    Store findOneByStoreId(String storeId);

    List<Store> findAll();
}
