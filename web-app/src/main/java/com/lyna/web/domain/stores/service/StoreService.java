package com.lyna.web.domain.stores.service;

import com.lyna.web.domain.stores.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface StoreService {
    List<Store> findAll(int tenantId);

    List<Store> getStoreList(int principal, String search);

    List<Store> getStoreList(int principal);

    @SuppressWarnings("unused")
    Page<Store> findPaginated(Pageable of, int tenantId, String searchText);

    Page<Store> findPaginated(int tenantId);

    String deleteStore(String storeIds);

    void createStore(Store store, UsernamePasswordAuthenticationToken principal);

    void updateStore(Store store, UsernamePasswordAuthenticationToken principal);

    Store findOneByStoreId(String storeId);
}
