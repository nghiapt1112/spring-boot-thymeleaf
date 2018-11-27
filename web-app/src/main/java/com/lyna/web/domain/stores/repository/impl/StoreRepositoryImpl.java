package com.lyna.web.domain.stores.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class StoreRepositoryImpl extends SimpleJpaRepository<Store, Long> implements StoreRepository {

//    @PersistenceContext
    private EntityManager em;

    public StoreRepositoryImpl(EntityManager em) {
        super(Store.class, em);
        this.em = em;
    }

    @Override
    public List<Store> getAll(int tenantId) {

        //ToDo: LinhNM: test if tenantId is null => has get info ?
        TypedQuery<Store> query =
                em.createNamedQuery("Store.getAll", Store.class)
                        .setParameter("tenantId", tenantId);
        List<Store> results = query.getResultList();

        return results;
    }

    @Override
    public List<Store> findAll(int tenantId) {
        return em
                .createQuery("SELECT s FROM Store s WHERE s.tenantId=:tenantId", Store.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }


}
