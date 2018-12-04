package com.lyna.web.domain.postCourse.repository;

import com.lyna.web.domain.postCourse.PostCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCourseRepository extends JpaRepository<PostCourse, Long> {
    List<PostCourse> findAllByStoreId(String storeId);

    void updatePostCourse(PostCourse postCourse);
}
