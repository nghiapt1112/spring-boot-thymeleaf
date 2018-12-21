package com.lyna.web.domain.mpackage.repository;

import com.lyna.web.domain.mpackage.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PackageRepository extends JpaRepository<Package, String> {
    List<Package> findByIds(int tenantId, Collection<String> ids);

    Package findOneByPackageIdAndTenantId(String packageId, int tenantId);

    boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId);

    List<Package> findByTenantId(int tenantId);

    List<Package> findAllByTenantId(int tenantId);
}
