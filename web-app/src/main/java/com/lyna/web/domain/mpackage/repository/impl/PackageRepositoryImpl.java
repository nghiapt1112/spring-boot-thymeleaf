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
import java.util.List;

@Repository
@Transactional
public class PackageRepositoryImpl extends BaseRepository<Package, Long> implements PackageRepository {

    private final Logger log = LoggerFactory.getLogger(PackageRepositoryImpl.class);

    public PackageRepositoryImpl(EntityManager em) {
        super(Package.class, em);
    }

    @Override
    public Package findOneByPackageId(String packageId) throws DomainException {
        try {
            return entityManager
                    .createQuery("SELECT p FROM Package p WHERE p.packageId=:packageId", Package.class)
                    .setParameter("packageId", packageId)
                    .getSingleResult();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean deleteByPackageIds(List<String> packageIds) throws DomainException {
        try {
            String query = "DELETE FROM Package p WHERE p.packageId in (:listPackageId)";
            entityManager.createQuery(query).setParameter("listPackageId", packageIds).executeUpdate();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Package> findByTenantId(int tenantId) {
        return entityManager
                .createQuery("SELECT p FROM Package p WHERE p.tenantId=:tenantId order by p.name", Package.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }
}
