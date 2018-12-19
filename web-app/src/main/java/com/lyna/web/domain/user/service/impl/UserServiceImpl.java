package com.lyna.web.domain.user.service.impl;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserAggregate;
import com.lyna.web.domain.user.UserResponsePage;
import com.lyna.web.domain.user.UserStoreAuthority;
import com.lyna.web.domain.user.exception.UserException;
import com.lyna.web.domain.user.repository.UserRepository;
import com.lyna.web.domain.user.repository.impl.UserStoreAuthorityRepositoryImpl;
import com.lyna.web.domain.user.service.UserService;
import com.lyna.web.domain.user.service.UserStoreAuthorityService;
import com.lyna.web.domain.view.UserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl extends BaseService implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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

        try {
            User createdUser = this.createUser(user.withDefaultFields(currentUser));
            userStoreAuthorityService.assignUserToStore(
                    aggregate.toUserStoreAuthorities()
                            .peek(el -> {
                                el.setUserId(createdUser.getId());
                                el.initDefaultCreateFields(currentUser);
                            })
                            .collect(Collectors.toList()));
            return createdUser;
        } catch (RuntimeException e) {
            throw new UserException(toInteger("err.user.createFailed.code"), toStr("err.user.createFailed.msg"));
        }
    }

    private void throwIfExisted(String email) {
        User userExisted = this.findByEmail(email);
        if (Objects.nonNull(userExisted)) {
            throw new UserException(toInteger("err.user.duplicateUser.code"), toStr("err.user.duplicateUser.msg"));
        }
    }

    public Page<UserList> findPaginated(List<Store> storeListAll, int tenantId) {
        List<UserList> listResults = new ArrayList<>();
        Map<String, Integer> mapStoreAuthority = new HashMap<>();
        int limit = getCountUser(tenantId);

        List<UserStoreAuthority> authorities = userStoreAuthority.findAll();
        List<User> listUser = userRepository.findAllByTenantId(tenantId);


        for (UserStoreAuthority authority : authorities) {
            mapStoreAuthority.put(authority.getUserId() + "_" + authority.getStoreId(), (int) authority.getAuthority());
        }

        for (User user : listUser) {
            UserList userList = new UserList();
            userList.setEmail(user.getEmail());
            userList.setName(user.getName());
            userList.setUserId(user.getId());
            userList.setRole(user.getRole());
            Map<String, Integer> map = new HashMap<>();

            for (Store store : storeListAll) {
                String sauthority = user.getId() + "_" + store.getStoreId();
                if (mapStoreAuthority != null && mapStoreAuthority.containsKey(sauthority)) {
                    map.put(store.getStoreId(), mapStoreAuthority.get(sauthority));
                } else {
                    map.put(store.getStoreId(), -1);
                }
            }

            userList.setMapStore(map);
            listResults.add(userList);
        }

        Page<UserList> userPage =
                new PageImpl<>(listResults, PageRequest.of(1, limit), listResults.size());

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
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        oldUser.updateInfo(userToUpdate);
        try {
            this.userRepository.save(oldUser);

            List<UserStoreAuthority> newUserStoreAuthority = aggregate.toUserStoreAuthorities().collect(Collectors.toList());

            Map<String, Short> authorityById = newUserStoreAuthority.stream()
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
            Set<String> oldAuthorities = oldUser.getStoreAuthoritiesAsStream().map(UserStoreAuthority::getId).collect(Collectors.toSet());


            this.userStoreAuthorityService.assignUserToStore(
                    newUserStoreAuthority.stream()
                            .filter(el -> !oldAuthorities.contains(el.getId()))
                            .peek(el -> {
                                el.setUserId(currentUser.getId());
                                el.setTenantId(currentUser.getTenantId());
                                el.initDefaultCreateFields(currentUser);
                            })
                            .collect(Collectors.toList())
            );
        } catch (RuntimeException e) {
            throw new UserException(toInteger("err.user.updateFailed.code"), toStr("err.user.updateFailed.msg"));
        }
    }

    @Override
    public UserResponsePage findUsersWithPaging(RequestPage userRequestPage) {
        UserResponsePage userResponPage = this.userRepository.findUsersWithPaging(userRequestPage);
        if (Objects.isNull(userRequestPage)) {
            throw new UserException(toInteger("err.user.pageError.code"), toStr("err.user.pageError.msg"));
        }
        return userResponPage;

    }

    @Override
    public int getCountUser(int tenantId) {
        List<User> userList = this.userRepository.findAllByTenantId(tenantId);
        if (Objects.isNull(userList)) {
            throw new UserException(toInteger("err.user.notFound.code"), toStr("err.user.notFound.msg"));
        }
        return userList.size();
    }

}
