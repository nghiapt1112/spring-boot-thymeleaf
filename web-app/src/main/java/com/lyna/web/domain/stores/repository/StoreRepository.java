package com.lyna.web.domain.stores.repository;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> getAll(User principal);

    Store save(Store store);
}
