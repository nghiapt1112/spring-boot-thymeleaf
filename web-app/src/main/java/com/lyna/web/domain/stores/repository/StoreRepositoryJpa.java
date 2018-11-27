package com.lyna.web.domain.stores.repository;

import com.lyna.web.domain.stores.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepositoryJpa extends JpaRepository<Store, Long> {
    public Store findOneByStoreId(String storeId);


}
