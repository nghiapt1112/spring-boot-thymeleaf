package com.lyna.web.domain.postCourse.sevice;

import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.stores.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCourseServiceImpl implements PostCourseService {
    @Autowired
    private PostCourseRepository postCourseRepository;

    @Override
    public void save(PostCourse postCourse) {
        postCourseRepository.save(postCourse);
    }

}
