package com.lyna.web.domain.stores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lyna.web.domain.stores.Store;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> getAll(int tenantId);

    Store save(Store store);

    List<Store> findAll(int tenantId);

    List<Store> getAll(int tenantId, String search);

    boolean deletebyStoreId(List<String> listStoreId);

    Store findOneByStoreId(String storeId);

    void updateStore(Store store);
}
