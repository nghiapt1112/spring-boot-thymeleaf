package com.lyna.web.domain.postCourse.repository;

import com.lyna.web.domain.postCourse.PostCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCourseRepository extends JpaRepository<PostCourse, String> {
    List<PostCourse> findAllByStoreIdAndTenantId(int tenantId, String storeId);

    String checkByStoreIdAndPost(String storeId, String post);

    boolean deleteByStoreIdsAndTenantId(List<String> storeIds, int tenantId);

    void deleteByPostCourseIdsAndTenantId(List<String> postCourseIds, int tenantId);

    List<String> findAllByStoreIdAndTenantId(int tenantId, List<String> storeIds);
}
