package com.lyna.web.domain.stores.repository;

import com.lyna.web.domain.stores.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> getAll(int tenantId);

    List<Store> findByTenantId(int tenantId);

    default List<Store> findAll(int tenantId) {
        return this.findByTenantId(tenantId);

    }

    List<String> getAllByCodesAndTenantId(int tenantId, List<String> storeCodes);

    List<Store> getAll(int tenantId, List<String> storeCodes);

    List<Store> getAll(int tenantId, String search);

    Store findOneByStoreIdAndTenantId(String storeId, int tenantId);

    Store findOneByCodeAndTenantId(String code, int tenantId);

    boolean deleteByStoreIdsAndTenantId(List<String> storeIds, int tenantId);


}
