package com.lyna.web.domain.postCourse.sevice;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PostCourseServiceImpl implements PostCourseService {

    @Autowired
    private PostCourseRepository postCourseRepository;


    @Override
    @Transactional
    public void updatePostCourse(PostCourse postCourse) throws DomainException {
        try {
            postCourseRepository.save(postCourse);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<PostCourse> findAllByStoreIdAndTenantId(String storeId, int tenantId) {
        return postCourseRepository.findAllByStoreIdAndTenantId(storeId, tenantId);
    }
}
