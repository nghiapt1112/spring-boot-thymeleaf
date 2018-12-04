package com.lyna.web.domain.postCourse.sevice;

import com.lyna.web.domain.postCourse.PostCourse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface PostCourseService {


    void updatePostCourse(List<PostCourse> postCourses, UsernamePasswordAuthenticationToken principal, String storeId);

    List<PostCourse> findAllByStoreId(String storeId);

}
