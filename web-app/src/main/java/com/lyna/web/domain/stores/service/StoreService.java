package com.lyna.web.domain.stores.service;

import com.lyna.web.domain.stores.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface StoreService {
    List<Store> findByTenantId(int tenantId);

    List<Store> getStoreList(int principal, String search);

    List<Store> getStoreList(int principal);

    @SuppressWarnings("unused")
    Page<Store> findPaginated(Pageable of, int tenantId, String searchText);

    Page<Store> findPaginated(int tenantId);

    String deleteStore(String storeIds);

    void create(Store store, UsernamePasswordAuthenticationToken principal);

    void update(Store store, UsernamePasswordAuthenticationToken principal);

    Store findOneByStoreId(String code);

    Store findOneByCode(String code);


}
