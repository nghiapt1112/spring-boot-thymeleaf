package com.lyna.web.domain.stores.service;

import com.lyna.web.domain.stores.Store;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface StoreService {
    List<Store> findAll(int tenantId);

    List<Store> getStoreList(int tenantId);

    void save(Store store);

    public Store findOneByStoreId(String storeId);

    public List<Store> findAll();
}
