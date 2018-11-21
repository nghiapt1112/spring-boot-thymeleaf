package com.lyna.web.domain.stores.service;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;

import java.util.List;

public interface StoreService {
    List<Store> findAll(int tenantId);

    List<Store> getStoreList(User principal);

}
