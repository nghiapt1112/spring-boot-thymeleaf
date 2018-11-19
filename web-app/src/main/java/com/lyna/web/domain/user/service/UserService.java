package com.lyna.web.domain.user.service;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserList;
import com.lyna.web.domain.user.UserRegisterAggregate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserService {
    User findByEmail(String userEmail);

    User registerUser(User currentUser, UserRegisterAggregate userRegisterAggregate);

    User createUser(User user);

    Page<UserList> findPaginated(Pageable pageable, List<Store> storeList);
}
