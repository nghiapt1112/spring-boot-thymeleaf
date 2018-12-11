package com.lyna.web.domain.mpackage.service;

import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.user.User;

import java.util.List;

public interface PackageService {

    void update(Package mpackage, User user);

    Package findOneByPakageIdAndTenantId(String pakageId, int tenantId);

    List<Package> findByTenantId(int tenantId);

    void create(Package mpackage, User user);

}
