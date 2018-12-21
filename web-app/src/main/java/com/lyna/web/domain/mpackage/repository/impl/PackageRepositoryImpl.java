package com.lyna.web.domain.mpackage.repository.impl;

import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Repository
public class PackageRepositoryImpl extends BaseRepository<Package, String> implements PackageRepository, PagingRepository {

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

    @Override
    public Package findOneByPackageIdAndTenantId(String packageId, int tenantId) {
        return super.entityManager
                .createQuery("SELECT p FROM Package p WHERE p.packageId=:packageId AND p.tenantId=:tenantId", Package.class)
                .setParameter("packageId", packageId)
                .setParameter("tenantId", tenantId)
                .getSingleResult();
    }

    @Override
    public boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId) {
        String query = "DELETE FROM Package p WHERE p.packageId in (:packageIds) AND p.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("packageIds", packageIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
        return true;
    }

    @Override
    public List<Package> findByTenantId(int tenantId) {
        return super.entityManager
                .createQuery("SELECT p FROM Package p WHERE p.tenantId=:tenantId order by p.name", Package.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

}
