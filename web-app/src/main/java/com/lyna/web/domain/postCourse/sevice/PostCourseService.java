package com.lyna.web.domain.postCourse.sevice;

import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.stores.Store;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;

import java.util.List;

public interface PostCourseService {


    void updatePostCourse(List<PostCourse> postCourses, UsernamePasswordAuthenticationToken principal, String storeId);

    List<PostCourse> findAllByStoreId(String storeId);

}
