package com.lyna.web.domain.user.repository;

import com.lyna.web.domain.user.UserStoreAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStoreAuthorityRepository extends JpaRepository<UserStoreAuthority, String> {
    Boolean deleteUserStoreAuthorityByUserIds(List<String> userIds, int tenantId);

    boolean deleteStoreAuthorityByStoreId(List<String> listStoreId);
}
