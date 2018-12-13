package com.lyna.web.domain.postCourse.sevice;

import com.lyna.web.domain.postCourse.PostCourse;

import java.util.List;

public interface PostCourseService {

    List<PostCourse> findAllByStoreIdAndTenantId(int tenantId, String storeId);

    boolean deleteByStoreIdsAndTenantId(List<String> storeIds, int tenantId);

}
