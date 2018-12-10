package com.lyna.web.domain.mpackage.service;

import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.stores.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface PackageService {

       void updatePackage(Package mpackage, UsernamePasswordAuthenticationToken principal);
       Package findOneByPakageId(String pakageId);
       List<Package> findAll(int tenantId);
       void createPackage(Package mpackage, UsernamePasswordAuthenticationToken principal);
       boolean deleteByPackageIds(List<String> packageIds);
 }
