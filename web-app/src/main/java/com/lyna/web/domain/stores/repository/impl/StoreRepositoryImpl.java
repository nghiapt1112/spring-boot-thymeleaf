package com.lyna.web.domain.stores.repository.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class StoreRepositoryImpl extends BaseRepository<Store, Long> implements StoreRepository {

    private final Logger log = LoggerFactory.getLogger(StoreRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public StoreRepositoryImpl(EntityManager em) {
        super(Store.class, em);
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
    public void updateStore(Store store) {

        try {
            String hql = "UPDATE Store s set s.tenantId = :tenantId, s.updateUser = :updateUser, s.updateDate = :updateDate,"
                    + "s.code = :code, s.name = :name, s.majorArea = :majorArea, s.area = :area, s.address = :address,"
                    + "s.personCharge = :personCharge, s.phoneNumber = :phoneNumber WHERE s.storeId=:storeId";
            entityManager.createQuery(hql)
                    .setParameter("tenantId", store.getTenantId())
                    .setParameter("updateUser", store.getUpdateUser())
                    .setParameter("updateDate", store.getUpdateDate())
                    .setParameter("code", store.getCode())
                    .setParameter("name", store.getName())
                    .setParameter("majorArea", store.getMajorArea())
                    .setParameter("area", store.getArea())
                    .setParameter("address", store.getAddress())
                    .setParameter("personCharge", store.getPersonCharge())
                    .setParameter("phoneNumber", store.getPhoneNumber())
                    .setParameter("storeId", store.getStoreId())
                    .executeUpdate();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
