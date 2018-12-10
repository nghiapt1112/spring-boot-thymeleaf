package com.lyna.web.domain.postCourse.sevice;

import com.lyna.web.domain.postCourse.PostCourse;

import java.util.List;

public interface PostCourseService {


    void updatePostCourse(PostCourse postCourse);

    List<PostCourse> findAllByStoreIdAndTenantId(String storeId, int tenantId);

}
