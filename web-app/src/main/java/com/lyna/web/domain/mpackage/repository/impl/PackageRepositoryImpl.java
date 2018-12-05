package com.lyna.web.domain.mpackage.repository.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.impl.StoreRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class PackageRepositoryImpl extends BaseRepository<Package, Long> implements PackageRepository {

    private final Logger log = LoggerFactory.getLogger(StoreRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public PackageRepositoryImpl(EntityManager em) {
        super(Package.class, em);
    }

    @Override
    public void updatePackage(Package mpackage) {
        try {
            String hql = "UPDATE Package p set p.tenantId = :tenantId, p.updateUser = :updateUser, p.updateDate = :updateDate,"
                    +"p.name = :name, p.unit = :unit, p.emptyWeight = :emptyWeight, p.fullLoadWeight = :fullLoadWeight,"
                    +"p.emptyCapacity = :emptyCapacity, p.fullLoadCapacity = :fullLoadCapacity WHERE p.packageId=:packageId";
            entityManager.createQuery(hql)
                    .setParameter("tenantId", mpackage.getTenantId())
                    .setParameter("updateUser", mpackage.getUpdateUser())
                    .setParameter("updateDate", mpackage.getUpdateDate())
                    .setParameter("name", mpackage.getName())
                    .setParameter("unit", mpackage.getUnit())
                    .setParameter("emptyWeight", mpackage.getEmptyWeight())
                    .setParameter("fullLoadWeight", mpackage.getFullLoadWeight())
                    .setParameter("emptyCapacity", mpackage.getEmptyCapacity())
                    .setParameter("fullLoadCapacity", mpackage.getFullLoadCapacity())
                    .setParameter("packageId", mpackage.getPackageId())
                    .executeUpdate();

        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @Override
    public Package findOneByPackageId(String packageId) {
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
    public boolean deletebyPackageId(List<String> listPackageId) throws DomainException {
        try {
            String query = "DELETE FROM Package p WHERE p.packageId in (:listPackageId)";
            entityManager.createQuery(query).setParameter("listPackageId", listPackageId).executeUpdate();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
