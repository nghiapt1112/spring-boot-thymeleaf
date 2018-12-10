package com.lyna.web.domain.mpackage.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.mpackage.service.PackageService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.repository.UserStoreAuthorityRepository;
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

    @Autowired
    private UserStoreAuthorityRepository userStoreAuthorityRepository;

    @Override
    public void updatePackage(Package mpackage, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        int tenantId = currentUser.getTenantId();
        String userId = currentUser.getId();
        Date date = new Date();
        mpackage.setUpdateDate(date);
        mpackage.setUpdateUser(userId);
        mpackage.setTenantId(tenantId);
        packageRepository.save(mpackage);
    }

    @Override
    @Transactional
    public void createPackage(Package mpackage, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        int tenantId = currentUser.getTenantId();
        String username = currentUser.getId();
        Date date = new Date();

        mpackage.setCreateDate(date);
        mpackage.setTenantId(tenantId);
        mpackage.setCreateUser(username);

        try {
            packageRepository.save(mpackage);
        }catch (NullPointerException ne){
            log.error(ne.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }

    @Override
    public Package findOneByPakageId(String pakageId) {
        return packageRepository.findOneByPackageId(pakageId);
    }

    @Override
    public List<Package> findAll(int tenantId) {
        return packageRepository.findAll(tenantId);
    }

    @Override
    public boolean deletebyPackageId(List<String> listPackageId){
        return packageRepository.deletebyPackageId(listPackageId);
    }
}
