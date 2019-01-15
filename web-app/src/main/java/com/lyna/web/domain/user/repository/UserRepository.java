package com.lyna.web.domain.user.repository;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserResponsePage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);

    List<User> findAll();

    User findByUserIdAndTenantId(int tenantId, String userId);

    UserResponsePage findUsersWithPaging(RequestPage userRequestPage);

    boolean updateProfileWithoutPassword(User user);

    Boolean deleteByUserIds(List<String> userIds, int tenantId);

    List<User> findAllByTenantId(int tenantId);

}
