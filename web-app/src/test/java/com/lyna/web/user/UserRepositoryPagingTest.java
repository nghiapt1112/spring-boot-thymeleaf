package com.lyna.web.user;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.object.SortType;
import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.user.UserRequestPage;
import com.lyna.web.domain.user.UserResponsePage;
import com.lyna.web.domain.user.repository.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class UserRepositoryPagingTest extends LynaApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUsers() {
        UserRequestPage req = new UserRequestPage();
        req.setCurrentPage(1);
        req.setNoOfRowInPage(10);


        Map<String, Object> searchFields = new HashMap<>();
        searchFields.put("start", "");
        searchFields.put("end", "");
        searchFields.put("uemail", "");
        req.setSearchFields(searchFields);


        Map<String, Object> sortFields = new HashMap<>();
        sortFields.put("name", SortType.ASC);
        sortFields.put("mail", SortType.DESC);
        req.setSortFields(sortFields);

        UserResponsePage pageResult = userRepository.findUserWithPaging(req);
        System.out.println(pageResult);
    }
}
