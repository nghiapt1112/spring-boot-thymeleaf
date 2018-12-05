package com.lyna.web.domain.mpackage.repository;

import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.stores.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Long> {
    void updatePackage(Package mpackage);
    Package findOneByPackageId(String packageId);
    boolean deletebyPackageId(List<String> listPackageId);
}
