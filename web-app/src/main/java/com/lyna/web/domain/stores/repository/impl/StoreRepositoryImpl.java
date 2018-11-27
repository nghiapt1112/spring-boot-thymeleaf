package com.lyna.web.domain.stores.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class StoreRepositoryImpl extends BaseRepository<Store, Long> implements StoreRepository {

    public StoreRepositoryImpl(EntityManager em) {
        super(Store.class, em);
     }

    @Override
    public List<Store> getAll(int tenantId) {

        //ToDo: LinhNM: test if tenantId is null => has get info ?
        TypedQuery<Store> query =
                entityManager.createNamedQuery("Store.getAll", Store.class)
                        .setParameter("tenantId", tenantId);
        List<Store> results = query.getResultList();

        return results;
    }

    @Override
    public List<Store> findAll(int tenantId) {
        return entityManager
                .createQuery("SELECT s FROM Store s WHERE s.tenantId=:tenantId", Store.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    public Store findOneByStoreId(String storeId) {
        return entityManager
                .createQuery("SELECT s FROM Store s WHERE s.storeId=:storeId", Store.class)
                .setParameter("storeId", storeId)
                .getSingleResult();
    }


}
