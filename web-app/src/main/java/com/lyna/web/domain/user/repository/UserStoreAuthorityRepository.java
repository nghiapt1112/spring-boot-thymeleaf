package com.lyna.web.domain.user.repository;

import com.lyna.web.domain.user.UserStoreAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoreAuthorityRepository extends JpaRepository<UserStoreAuthority, String> {

}
