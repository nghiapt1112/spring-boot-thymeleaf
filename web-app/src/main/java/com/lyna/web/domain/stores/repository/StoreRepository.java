package com.lyna.web.domain.stores.repository;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.view.CsvStore;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, String>, PagingRepository {
    Iterator<CsvStore> getMapStore(Reader targetReader);

    List<Store> getAll(int tenantId);

    List<Store> findByTenantId(int tenantId);

    default List<Store> findAll(int tenantId) {
        return this.findByTenantId(tenantId);

    }

    List<String> getAllByCodesAndTenantId(int tenantId, List<String> storeCodes);

    List<Store> getAll(int tenantId, List<String> storeCodes);

    List<Store> getAll(int tenantId, String search);

    Store findOneByStoreIdAndTenantId(String storeId, int tenantId);

    Store findByCodeAndTenantId(String code, int tenantId);

    boolean deleteByStoreIdsAndTenantId(List<String> storeIds, int tenantId);

//    List<Store> getAllByStoreCodesAndTenantId(List<String> storeCodes, int tenantId);
}
