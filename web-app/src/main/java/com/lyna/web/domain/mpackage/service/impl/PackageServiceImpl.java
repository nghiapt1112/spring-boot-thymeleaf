package com.lyna.web.domain.mpackage.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.mpackage.service.PackageService;
import com.lyna.web.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    public void update(Package mpackage, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        Date date = new Date();
        mpackage.setUpdateDate(date);
        mpackage.setUpdateUser(currentUser.getId());
        mpackage.setTenantId(currentUser.getTenantId());
        try {
            packageRepository.save(mpackage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Override
    @Transactional
    public void create(Package mpackage, UsernamePasswordAuthenticationToken principal) throws DomainException {
        User currentUser = (User) principal.getPrincipal();
        Date date = new Date();
        mpackage.setCreateDate(date);
        mpackage.setTenantId(currentUser.getTenantId());
        mpackage.setCreateUser(currentUser.getId());

        try {
            packageRepository.save(mpackage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public Package findOneByPakageId(String pakageId) {
        return packageRepository.findOneByPackageId(pakageId);
    }

    @Override
    public List<Package> findByTenantId(int tenantId) {
        return packageRepository.findByTenantId(tenantId);
    }

}
