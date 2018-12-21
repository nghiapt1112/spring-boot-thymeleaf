package com.lyna.web.domain.mpackage.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.exception.PackageException;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.mpackage.service.PackageService;
import com.lyna.web.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PackageServiceImpl extends BaseService implements PackageService {

    private final Logger log = LoggerFactory.getLogger(PackageServiceImpl.class);

    @Autowired
    private PackageRepository packageRepository;

    @Override
    @Transactional
    public void update(Package mpackage, User user) {
        Date date = new Date();
        mpackage.setUpdateDate(date);
        mpackage.setUpdateUser(user.getId());
        try {
            packageRepository.save(mpackage);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PackageException(toInteger("err.package.updateError.code"), toStr("err.package.updateError.msg"));
        }

    }

    @Override
    @Transactional
    public void create(Package mpackage, User user) {
        Date date = new Date();
        mpackage.setCreateDate(date);
        mpackage.setTenantId(user.getTenantId());
        mpackage.setCreateUser(user.getId());

        try {
            packageRepository.save(mpackage);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PackageException(toInteger("err.package.createError.code"), toStr("err.package.createError.msg"));
        }

    }

    @Override
    public Package findOneByPakageIdAndTenantId(String pakageId, int tenantId) {
        try {
            return packageRepository.findOneByPackageIdAndTenantId(pakageId, tenantId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PackageException(toInteger("err.package.notFound.code"), toStr("err.package.notFound.msg"));
        }
    }

    @Override
    public List<Package> findByTenantId(int tenantId) {
        try {
            return packageRepository.findByTenantId(tenantId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PackageException(toInteger("err.package.notFound.code"), toStr("err.package.notFound.msg"));
        }

    }

}
