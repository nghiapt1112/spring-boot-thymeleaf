package com.lyna.web.domain.postCourse.sevice;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostCourseServiceImpl extends BaseService implements PostCourseService {

    private final Logger log = LoggerFactory.getLogger(PostCourseServiceImpl.class);

    @Autowired
    private PostCourseRepository postCourseRepository;


    @Override
    @Transactional
    public void updatePostCourse(PostCourse postCourse) throws DomainException {
        try {
            postCourseRepository.save(postCourse);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<PostCourse> findAllByStoreIdAndTenantId(String storeId, int tenantId) {
        return postCourseRepository.findAllByStoreIdAndTenantId(storeId, tenantId);
    }
}
