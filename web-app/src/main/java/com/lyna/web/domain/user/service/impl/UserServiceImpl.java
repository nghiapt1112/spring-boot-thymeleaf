package com.lyna.web.domain.user.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserAggregate;
import com.lyna.web.domain.user.UserStoreAuthority;
import com.lyna.web.domain.user.exception.UserException;
import com.lyna.web.domain.user.repository.UserRepository;
import com.lyna.web.domain.user.repository.impl.UserStoreAuthorityRepositoryImpl;
import com.lyna.web.domain.user.service.UserService;
import com.lyna.web.domain.user.service.UserStoreAuthorityService;
import com.lyna.web.domain.view.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Transactional
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStoreAuthorityService userStoreAuthorityService;


    @Autowired
    private UserStoreAuthorityRepositoryImpl userStoreAuthority;

    @Override
    public User findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User registerUser(User currentUser, UserAggregate aggregate) {
        User user = aggregate.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        this.throwIfExisted(user.getEmail());

        User createdUser = this.createUser(user.withDefaultFields(currentUser));
        userStoreAuthorityService.assignUserToStore(
                aggregate.toUserStoreAuthorities()
                        .peek(el -> {
                            el.setUserId(createdUser.getId());
                            el.initDefaultCreateFields(currentUser);
                        })
                        .collect(Collectors.toList()));
        return createdUser;
    }

    private void throwIfExisted(String email) {
        User userExisted = this.findByEmail(email);
        if (Objects.nonNull(userExisted)) {
            throw new UserException(toInteger("err.user.duplicateUser.code"), toStr("err.user.duplicateUser.msg"));
        }
    }

    public Page<UserList> findPaginated(Pageable pageable, List<Store> storeListAll) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<UserList> listUserList = new ArrayList<>();
        List<UserList> listResponse = new ArrayList<>();
        Map<String, String> mapStore = new HashMap<>();
        Map<String, UserStoreAuthority> mapStoreAuthority = new HashMap<>();

        //storeListAll = storeRepository.getAll();
        List<UserStoreAuthority> authorities = userStoreAuthority.findAll();
        List<User> userListIn = userRepository.findAll();

        for (Store store : storeListAll) {
            mapStore.put(store.getStoreId(), store.getName());
        }

        for (UserStoreAuthority authority : authorities) {
            mapStoreAuthority.put(authority.getUserId(), authority);
        }

        for (User user : userListIn) {
            UserList userList = new UserList();
            userList.setEmail(user.getEmail());
            userList.setName(user.getName());
            userList.setUserId(user.getId());
            userList.setRole(user.getRole());

            UserStoreAuthority userStoreAuthority = mapStoreAuthority.get(user.getId());
            Map<String, Integer> map = new HashMap<>();

            //TODO Liệt kê toàn bộ store name theo id, sau đó đối với storeId thì thêm quyền của nó vào
            //So sanh userId co quyen de lay ra duoc authority
            for (Map.Entry<String, String> entry : mapStore.entrySet()) {
                if (userStoreAuthority != null && userStoreAuthority.getStoreId().equals(entry.getKey())) {
                    map.put(entry.getValue(), (int) userStoreAuthority.getAuthority());
                } else {
                    map.put(entry.getValue(), 0);
                }
            }

            userList.setMapStore(map);
            listUserList.add(userList);
        }


        if (userListIn.size() < startItem) {
            listUserList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, userListIn.size());
            listResponse = listUserList.subList(startItem, toIndex);
        }

        Page<UserList> userPage =
                new PageImpl<UserList>(listResponse, PageRequest.of(currentPage, pageSize), listUserList.size());

        return userPage;

    }

    @Override
    public User findById(int tenantId, String userId) {
        User user = this.userRepository.findById(tenantId, userId);
        if (Objects.isNull(user)) {
            throw new UserException(toInteger("err.user.notFound.code"), toStr("err.user.notFound.msg"));
        }
        return user;
    }

    @Override
    @Transactional
    public void update(User currentUser, UserAggregate aggregate) {
        User oldUser = this.findById(currentUser.getTenantId(), aggregate.getUserId());
        User userToUpdate = aggregate.toUser();
        oldUser.updateInfo(userToUpdate);
        this.userRepository.save(oldUser);


        Map<String, Short> authorityById = aggregate.toUserStoreAuthorities()
                .collect(Collectors.toMap(UserStoreAuthority::getId, o -> o.getAuthority(), (v1, v2) -> v1));

        this.userStoreAuthorityService.assignUserToStore(
                oldUser.getStoreAuthoritiesAsStream()
                        .filter(el -> authorityById.containsKey(el.getId()))
                        .peek(el -> {
                            el.setAuthority(authorityById.get(el.getId()));
                            el.initDefaultUpdateFields(currentUser);
                        })
                        .collect(Collectors.toList())
        );

    }
	
	 @Override
    public String deleteUser(String sUserId) {
        boolean isDeletedUser = false;
        List<String> listUserId = new ArrayList();

        String[] arrayUserId = sUserId.split(",");
        for (String userId : arrayUserId) {
            listUserId.add(userId);
        }

        try {
            boolean isDeletedStoreAuthority = userStoreAuthority.deletebyUserId(listUserId);
            if (isDeletedStoreAuthority) {
                isDeletedUser = userRepository.deleteByUserId(listUserId);
                if (!isDeletedUser) {
                    //ToDo: After delete is not success, you need rollback to data!!! Impotant
                    userRepository.commitTransaction();
                }
            }
        } catch (Exception ex) {
        }

        if (isDeletedUser)
            return "Success";
        else
            return null;
    }

}
