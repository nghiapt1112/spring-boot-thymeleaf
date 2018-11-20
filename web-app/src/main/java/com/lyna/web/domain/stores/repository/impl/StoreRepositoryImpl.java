package com.lyna.web.domain.stores.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.user.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class StoreRepositoryImpl extends BaseRepository<Store, Long> implements StoreRepository {

    @PersistenceContext
    private EntityManager em;

    public StoreRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public List<Store> getAll(User principal) {
        TypedQuery<Store> query =
                em.createNamedQuery("Store.getAll", Store.class)
                        .setParameter("tenantId", principal.getTenantId());
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
}
