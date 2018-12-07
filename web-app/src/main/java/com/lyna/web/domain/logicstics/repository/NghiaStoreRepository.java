package com.lyna.web.domain.logicstics.repository;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NghiaStoreRepository extends JpaRepository<Store, String>, PagingRepository {
}
