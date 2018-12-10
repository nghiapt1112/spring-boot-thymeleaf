package com.lyna.web.domain.postCourse.sevice;

import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.stores.Store;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface PostCourseService {


    void updatePostCourse(PostCourse postCourse);

    List<PostCourse> findAllByStoreIdAndTenantId(String storeId, int tenantId);

}
