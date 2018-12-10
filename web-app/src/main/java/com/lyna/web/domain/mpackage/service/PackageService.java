package com.lyna.web.domain.mpackage.service;

import com.lyna.web.domain.mpackage.Package;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface PackageService {

       void update(Package mpackage, UsernamePasswordAuthenticationToken principal);
       Package findOneByPakageId(String pakageId);
       List<Package> findByTenantId(int tenantId);
       void create(Package mpackage, UsernamePasswordAuthenticationToken principal);
       boolean deleteByPackageIds(List<String> packageIds);
 }
