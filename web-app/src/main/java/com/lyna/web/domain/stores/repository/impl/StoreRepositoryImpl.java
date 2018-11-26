package com.lyna.web.domain.stores.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class StoreRepositoryImpl extends BaseRepository<Store, Long> implements StoreRepository {

    private final Logger log = LoggerFactory.getLogger(StoreRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public StoreRepositoryImpl(EntityManager em) {
        super(User.class, em);
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
    @Transactional
    public Store save(Store store) {
        if (store.getStoreId() == null) {
            em.persist(store);
            return store;
        } else {
            return em.merge(store);
        }
    }

    @Override
    public List<Store> findAll(int tenantId) {
        return entityManager
                .createQuery("SELECT s FROM Store s WHERE s.tenantId=:tenantId", Store.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    public List<Store> getAll(int tenantId, String search) {
        return entityManager
                .createQuery("SELECT s FROM Store s WHERE s.tenantId=:tenantId and (s.code like '%:search%' or s.name like '%:search%' or s.majorArea like '%:search%' or s.area like '%:search%')", Store.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    public boolean deletebyStoreId(List<String> listStoreId) {
        try {
            String query = "DELETE FROM Store u WHERE u.storeId in (:storelist)";
            entityManager.createQuery(query).setParameter("storelist", listStoreId).executeUpdate();
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;
    }
}
