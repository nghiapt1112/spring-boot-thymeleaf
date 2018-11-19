package com.lyna.web.domain.stores.repository;

import com.lyna.web.domain.user.UserStoreAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStoreAuthorityRepository extends JpaRepository<UserStoreAuthority, Long> {
    List<UserStoreAuthority> findAll();
}
