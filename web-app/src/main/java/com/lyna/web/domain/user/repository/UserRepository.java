package com.lyna.web.domain.user.repository;

import com.lyna.web.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    List<User> findAll();

    User findById(int tenantId, String userId);
	Boolean deleteByUserId(List<String> names);
    void commitTransaction();
}
