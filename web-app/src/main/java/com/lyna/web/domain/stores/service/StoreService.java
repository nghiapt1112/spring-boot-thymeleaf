package com.lyna.web.domain.stores.service;

import com.lyna.web.domain.stores.Store;

import java.util.List;

public interface StoreService {
    List<Store> findAll(int tenantId);

    List<Store> getStoreList(int tenantId);
}
