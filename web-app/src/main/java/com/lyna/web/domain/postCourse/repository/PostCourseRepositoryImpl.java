package com.lyna.web.domain.postCourse.repository;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.postCourse.PostCourse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class PostCourseRepositoryImpl extends BaseRepository<PostCourse, Long> implements PostCourseRepository {

    private final Logger log = LoggerFactory.getLogger(PostCourseRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public PostCourseRepositoryImpl(EntityManager em) {
        super(PostCourse.class, em);
    }

    @Override
    public List<PostCourse> findAllByStoreIdAndTenantId(String storeId, int tenantId) {
        return entityManager
                .createQuery("SELECT p FROM PostCourse p WHERE p.storeId = :storeId AND p.tenantId = :tenantId", PostCourse.class)
                .setParameter("storeId", storeId)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    /*@Override
    public void updatePostCourse(PostCourse postCourse) throws DomainException {
        try {
            String hql = "UPDATE PostCourse p set p.tenantId = :tenantId, p.updateUser = :updateUser, p.updateDate = :updateDate,"
                    + "p.post = :post, p.course = :course WHERE p.postCourseId=:postCourseId";
            entityManager.createQuery(hql)
                    .setParameter("tenantId", postCourse.getTenantId())
                    .setParameter("updateUser", postCourse.getUpdateUser())
                    .setParameter("updateDate", postCourse.getUpdateDate())
                    .setParameter("post", postCourse.getPost())
                    .setParameter("course", postCourse.getCourse())
                    .setParameter("postCourseId", postCourse.getPostCourseId())
                    .executeUpdate();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }*/
}
