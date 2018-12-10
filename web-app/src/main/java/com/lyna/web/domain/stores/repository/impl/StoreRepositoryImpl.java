package com.lyna.web.domain.stores.repository.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class StoreRepositoryImpl extends BaseRepository<Store, Long> implements StoreRepository {

    private final Logger log = LoggerFactory.getLogger(StoreRepositoryImpl.class);

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
    public List<Store> findByTenantId(int tenantId) {
        return entityManager
                .createQuery("SELECT s FROM Store s WHERE s.tenantId=:tenantId order by s.code,s.name", Store.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    public List<String> getAllByCode(int tenantId, List<String> storeCodes) {
        if (storeCodes.size() > 0) {
            Query query = entityManager.createQuery("SELECT s.code FROM Store s WHERE s.tenantId=:tenantId and s.code in (:code) order by s.code,s.name", String.class);
            query.setParameter("tenantId", tenantId)
                    .setParameter("code", storeCodes);
            return query.getResultList();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Store> getAll(int tenantId, List<String> storeCodes) throws DomainException {
        try {
            Query query = entityManager.createQuery("SELECT s FROM Store s WHERE s.tenantId=:tenantId and s.code in (:storeCodes) order by s.code,s.name", Store.class);
            query.setParameter("tenantId", tenantId)
                    .setParameter("storeCodes", storeCodes);
            List list = query.getResultList();
            return list;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<Store> getAll(int tenantId, String search) throws DomainException {
        String hql = "SELECT s FROM Store s WHERE s.tenantId=:tenantId";
        if (!search.isEmpty())
            hql = hql + " and (trim(lower(s.code)) like :search " +
                    "or trim(lower(s.name)) like :search " +
                    "or trim(lower(s.majorArea)) like :search " +
                    "or trim(lower(s.area)) like :search)";

        TypedQuery<Store> query = entityManager
                .createQuery(hql, Store.class);
        try {
            if (!search.isEmpty())
                return query.setParameter("tenantId", tenantId)
                        .setParameter("search", "%" + search.trim().toLowerCase() + "%")
                        .getResultList();
            else
                return query.setParameter("tenantId", tenantId)
                        .getResultList();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean deletebyStoreId(List<String> listStoreId) throws DomainException {
        try {
            String query = "DELETE FROM Store u WHERE u.storeId in (:storelist)";
            entityManager.createQuery(query).setParameter("storelist", listStoreId).executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Store findOneByStoreId(String storeId) {
        return entityManager
                .createQuery("SELECT s FROM Store s WHERE s.storeId=:storeId", Store.class)
                .setParameter("storeId", storeId)
                .getSingleResult();
    }

    @Override
    public Store findOneByCode(String code) {
        return entityManager
                .createQuery("SELECT s FROM Store s WHERE s.code=:code", Store.class)
                .setParameter("code", code)
                .getSingleResult();
    }

}
