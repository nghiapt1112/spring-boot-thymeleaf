package com.lyna.web.domain.mpackage.repository;

import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.view.CsvPackage;
import com.lyna.web.domain.view.CsvProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface PackageRepository extends JpaRepository<Package, String> {
    Iterator<CsvPackage> getMapPackage(Reader targetReader);

    List<Package> findByIds(int tenantId, Collection<String> ids);

    List<Package> getAllByNameAndTenantId(List<String> packageNames,int tenantId);

    Package findOneByPackageIdAndTenantId(String packageId, int tenantId);

    Package findOneByNameAndTenantId(String name, int tenantId);

    boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId);

    List<Package> findByTenantId(int tenantId);

}
