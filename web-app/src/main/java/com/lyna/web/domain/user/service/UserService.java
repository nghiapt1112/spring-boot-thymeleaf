package com.lyna.web.domain.user.service;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserAggregate;
import com.lyna.web.domain.view.UserList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserService {
    User findByEmail(String userEmail);

    User registerUser(User currentUser, UserAggregate userRegisterAggregate);

    User createUser(User user);

    Page<UserList> findPaginated(Pageable pageable, List<Store> storeList);

    User findById(int tenantId, String userId);

    void update(User currentUser, UserAggregate aggregate);
}
