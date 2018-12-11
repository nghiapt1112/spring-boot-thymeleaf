package com.lyna.web.domain.postCourse.sevice;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.stores.repository.StoreRepository;
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

    @Autowired
    private StoreRepository storeRepository;

    @Override
    @Transactional
    public void update(PostCourse postCourse) throws DomainException {
        try {
            postCourseRepository.save(postCourse);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<PostCourse> findAllByStoreId(String storeId) {
        return postCourseRepository.findAllByStoreId(storeId);
    }

    @Override
    @Transactional
    public boolean deleteByStoreIds(List<String> storeIds) {
        postCourseRepository.deleteByStoreIds(storeIds);
        storeRepository.deleteByStoreIds(storeIds);
        return true;
    }
}
