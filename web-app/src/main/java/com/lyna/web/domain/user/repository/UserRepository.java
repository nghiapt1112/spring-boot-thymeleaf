package com.lyna.web.domain.user.repository;

import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserResponsePage;
import com.lyna.commons.infrustructure.object.RequestPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);

    List<User> findAll();

    User findById(int tenantId, String userId);

    UserResponsePage findUsersWithPaging(RequestPage userRequestPage);

    Boolean deleteByUserId(List<String> names);

    List<User> findAllByTenantId(int tenantId);

}
