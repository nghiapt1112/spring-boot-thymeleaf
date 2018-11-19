package com.lyna.web.domain.user.repository;

import com.lyna.web.domain.user.UserStoreAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserStoreAuthorityRepository extends JpaRepository<UserStoreAuthority, String> {

    void assignUserToStore(Collection<UserStoreAuthority> userStoreAuthorities);
}
