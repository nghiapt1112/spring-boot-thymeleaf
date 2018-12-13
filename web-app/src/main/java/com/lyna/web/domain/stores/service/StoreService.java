package com.lyna.web.domain.stores.service;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    List<Store> findByTenantId(int tenantId);

    default List<Store> findAll(int tenantId) {
        return this.findByTenantId(tenantId);
    }

    List<Store> getStoreList(int principal, String search);

    List<Store> getStoreList(int principal);

    Page<Store> findPaginated(Pageable of, int tenantId, String searchText);

    Page<Store> findPaginated(int tenantId);

    String deleteStoreAndTenantId(List<String> storeIds, int tenantId);

    void create(Store store, User user);

    void update(Store store, User user);

    Store findOneByStoreIdAndTenantId(String code, int tenantId);

    Store findOneByCodeAndTenantId(String code, int tenantId);


}
