package com.lyna.web.domain.stores.service;

import com.lyna.web.domain.stores.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    List<Store> findAll(int tenantId);

    List<Store> getStoreList(int principal, String search);

    List<Store> getStoreList(int principal);

    Page<Store> findPaginated(Pageable of, int tenantId, String searchText);

    String deleteStore(String storeIds);
}
