package com.lyna.web.domain.mpackage.repository;

import com.lyna.web.domain.mpackage.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package, String> {
    Package findOneByPackageId(String packageId);

    boolean deleteByPackageIds(List<String> packageIds);

    List<Package> findByTenantId(int tenantId);
}
