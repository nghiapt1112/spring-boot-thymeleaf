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
public class PostCourseRepositoryImpl extends BaseRepository<PostCourse, String> implements PostCourseRepository {

    private final Logger log = LoggerFactory.getLogger(PostCourseRepositoryImpl.class);

    public PostCourseRepositoryImpl(EntityManager em) {
        super(PostCourse.class, em);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostCourse> findAllByStoreIdAndTenantId(int tenantId, String storeId) {
        return entityManager
                .createQuery("SELECT p FROM PostCourse p WHERE p.storeId = :storeId AND p.tenantId = :tenantId", PostCourse.class)
                .setParameter("storeId", storeId)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public String checkByStoreIdAndPost(String storeId, String post) {
        List list = entityManager
                .createQuery("SELECT p.postCourseId FROM PostCourse p WHERE p.storeId = :storeId and p.post = :post")
                .setParameter("storeId", storeId)
                .setParameter("post", post)
                .getResultList();
        if (list != null && list.size() > 0)
            return (String) list.get(0);

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public PostCourse save(PostCourse postCourse) {
        if (postCourse.getPostCourseId() == null) {
            entityManager.persist(postCourse);
            return postCourse;
        } else {
            return entityManager.merge(postCourse);
        }
    }

    @Override
    public boolean deleteByStoreIdsAndTenantId(List<String> storeIds, int tenantId) {
        String query = "DELETE FROM PostCourse p WHERE p.storeId in (:storeIds) AND p.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("storeIds", storeIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
        return true;

    }
}
