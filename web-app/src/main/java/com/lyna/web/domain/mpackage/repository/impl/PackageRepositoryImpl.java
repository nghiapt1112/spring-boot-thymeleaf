package com.lyna.web.domain.mpackage.repository.impl;

import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import com.lyna.web.infrastructure.repository.PagingRepository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

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

}
