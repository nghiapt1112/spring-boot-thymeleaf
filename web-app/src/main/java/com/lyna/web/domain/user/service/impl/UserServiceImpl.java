package com.lyna.web.domain.user.service.impl;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.UserStoreAuthorityRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserList;
import com.lyna.web.domain.user.UserStoreAuthority;
import com.lyna.web.domain.user.repository.UserRepository;
import com.lyna.web.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;



    @Autowired
    private UserStoreAuthorityRepository userStoreAuthority;

    @Override
    public User findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
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
            userList.setName(user.getUsername());

            UserStoreAuthority userStoreAuthority = mapStoreAuthority.get(user.getUserId());
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
}
