package com.lyna.web.domain.postCourse.repository;

import com.lyna.web.domain.postCourse.PostCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCourseRepository extends JpaRepository<PostCourse, String> {
    List<PostCourse> findAllByStoreIdAndTenantId(int tenantId, String storeId);

    void updatePostCourse(PostCourse postCourse);

    String checkByStoreIdAndPost(String s, String s2);

    boolean deleteByStoreIdsAndTenantId(List<String> storeIds, int tenantId);

}
