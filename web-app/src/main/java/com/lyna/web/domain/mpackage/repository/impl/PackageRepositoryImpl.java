package com.lyna.web.domain.mpackage.repository.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Repository
public class PackageRepositoryImpl extends BaseRepository<Package, String> implements PackageRepository {

    private final Logger log = LoggerFactory.getLogger(PackageRepositoryImpl.class);

    public PackageRepositoryImpl(EntityManager em) {
        super(Package.class, em);
    }

    @Override
    public List<Package> findByIds(int tenantId, Collection<String> ids) {
        return super.entityManager
                .createQuery("SELECT p FROM Package p WHERE p.tenantId = :tenantId AND p.pakageId IN (:ids)", Package.class)
                .setParameter("tenantId", tenantId).setParameter("ids", ids)
                .getResultList();
    }

    @Transactional
    public Package findOneByPackageIdAndTenantId(String packageId, int tenantId) throws DomainException {
        return entityManager
                .createQuery("SELECT p FROM Package p WHERE p.packageId=:packageId AND p.tenantId=:tenantId", Package.class)
                .setParameter("packageId", packageId)
                .setParameter("tenantId", tenantId)
                .getSingleResult();
    }

    @Override
    public boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId) throws DomainException {
        String query = "DELETE FROM Package p WHERE p.packageId in (:packageIds) AND p.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("packageIds", packageIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
        return true;
    }

    @Override
    @Transactional
    public List<Package> findByTenantId(int tenantId) {

        return entityManager
                .createQuery("SELECT p FROM Package p WHERE p.tenantId=:tenantId order by p.name", Package.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    public List<Package> getListByName(int tenantId) {
        return super.entityManager
                .createQuery("SELECT p FROM Package p WHERE p.tenantId = :tenantId ", Package.class)
                .setParameter("tenantId", tenantId).setParameter("tenantId", tenantId)
                .getResultList();
    }

}
