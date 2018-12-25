package com.lyna.web.domain.user.service;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserAggregate;
import com.lyna.web.domain.user.UserResponsePage;
import com.lyna.web.domain.view.UserList;
import org.springframework.data.domain.Page;

import java.util.List;


public interface UserService {
    User findByEmail(String userEmail);

    User findByEmailAndTenantId(String email, int tenantId);

    User registerUser(User currentUser, UserAggregate userRegisterAggregate);

    User createUser(User user);

    //@SuppressWarnings("unused")
    //Page<UserList> findPaginated(Pageable pageable, List<Store> storeList, int tenantId);

    Page<UserList> findPaginated(List<Store> storeList, int tenantId);

    User findById(int tenantId, String userId);

    void update(User currentUser, UserAggregate aggregate);

    UserResponsePage findUsersWithPaging(RequestPage userRequestPage);

    int getCountUser(int tenantId);
}
