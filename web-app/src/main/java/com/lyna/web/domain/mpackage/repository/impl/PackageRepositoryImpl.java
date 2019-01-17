package com.lyna.web.domain.mpackage.repository.impl;

import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.view.CsvPackage;
import com.lyna.web.domain.view.CsvProduct;
import com.lyna.web.infrastructure.repository.BaseRepository;
import com.lyna.web.infrastructure.repository.PagingRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Repository
public class PackageRepositoryImpl extends BaseRepository<Package, String> implements PackageRepository, PagingRepository {

    public PackageRepositoryImpl(EntityManager em) {
        super(Package.class, em);
    }

    @Override
    public Iterator<CsvPackage> getMapPackage(Reader targetReader) {
        CsvToBean<CsvPackage> csvToBean = new CsvToBeanBuilder(targetReader)
                .withType(CsvPackage.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        Iterator<CsvPackage> csvPackageIterator = csvToBean.iterator();
        return csvPackageIterator;
    }

    @Override
    public List<Package> findByIds(int tenantId, Collection<String> ids) {
        return super.entityManager
                .createQuery("SELECT p FROM Package p WHERE p.tenantId = :tenantId AND p.pakageId IN (:ids)", Package.class)
                .setParameter("tenantId", tenantId).setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<Package> getAllByNameAndTenantId(List<String> packageNames,int tenantId) {
        return entityManager
                .createQuery("SELECT p FROM Package p WHERE p.name in (:packageNames) AND  p.tenantId=:tenantId")
                .setParameter("tenantId", tenantId)
                .setParameter("packageNames", packageNames)
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
    public Package findOneByNameAndTenantId(String name, int tenantId) {
        String query = "SELECT p FROM Package p WHERE p.name=:name AND p.tenantId=:tenantId";
        List<Package> packages = entityManager.createQuery(query, Package.class)
                .setParameter("name", name)
                .setParameter("tenantId", tenantId)
                .getResultList();
        return CollectionUtils.isEmpty(packages) ? null : packages.get(0);
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
                .createQuery("SELECT p FROM Package p WHERE p.tenantId=:tenantId ORDER BY p.name", Package.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

}
