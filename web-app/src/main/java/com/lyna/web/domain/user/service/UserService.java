package com.lyna.web.domain.user.service;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.List;


public interface UserService {
    User findByEmail(String userEmail);
    Page<UserList> findPaginated(Pageable pageable,  List<Store> storeList);
}
