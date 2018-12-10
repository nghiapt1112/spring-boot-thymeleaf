package com.lyna.web.domain.stores.repository;

import com.lyna.web.domain.stores.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> getAll(int tenantId);

    List<Store> findByTenantId(int tenantId);

    List<Store> getAll(int tenantId, String search);

    boolean deletebyStoreId(List<String> listStoreId);

    Store findOneByStoreId(String storeId);

    Store findOneByCode(String code);


}
