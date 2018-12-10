package com.lyna.web.domain.postCourse.repository;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.postCourse.PostCourse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class PostCourseRepositoryImpl extends BaseRepository<PostCourse, Long> implements PostCourseRepository {

    private final Logger log = LoggerFactory.getLogger(PostCourseRepositoryImpl.class);

    public PostCourseRepositoryImpl(EntityManager em) {
        super(PostCourse.class, em);
    }

    @Override
    public List<PostCourse> findAllByStoreId(String storeId) {
        return entityManager
                .createQuery("SELECT p FROM PostCourse p WHERE p.storeId = :storeId", PostCourse.class)
                .setParameter("storeId", storeId)
                .getResultList();
    }
}
