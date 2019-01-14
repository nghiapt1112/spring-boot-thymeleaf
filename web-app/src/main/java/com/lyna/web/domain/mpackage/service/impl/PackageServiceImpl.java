package com.lyna.web.domain.mpackage.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import com.lyna.web.domain.logicstics.repository.LogisticDetailRepository;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.exception.PackageException;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.mpackage.service.PackageService;
import com.lyna.web.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class PackageServiceImpl extends BaseService implements PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;

    @Autowired
    private LogisticDetailRepository logisticDetailRepository;

    @Override
    @Transactional
    public void update(Package mpackage, User user) {
        Date date = new Date();
        mpackage.setUpdateDate(date);
        mpackage.setUpdateUser(user.getId());

        List<DeliveryDetail> deliveryDetails = deliveryDetailRepository.findByTenantIdAndPackageId(user.getTenantId(), mpackage.getPackageId());
        if (!Objects.isNull(deliveryDetails)) {
            mpackage.setDeliveryDetails(new HashSet<DeliveryDetail>(deliveryDetails));
        }

        List<LogiticsDetail> logiticsDetails = logisticDetailRepository.findByTenantIdAndPackageId(user.getTenantId(), mpackage.getPackageId());
        if (!Objects.isNull(logiticsDetails)) {
            mpackage.setLogiticsDetails(new HashSet<LogiticsDetail>(logiticsDetails));
        }

        try {
            packageRepository.save(mpackage);
        } catch (Exception e) {
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
            throw new PackageException(toInteger("err.package.createError.code"), toStr("err.package.createError.msg"));
        }

    }

    @Override
    public Package findOneByPakageIdAndTenantId(String pakageId, int tenantId) {
        try {
            return packageRepository.findOneByPackageIdAndTenantId(pakageId, tenantId);
        } catch (Exception e) {
            throw new DomainException(toInteger("err.general.notFound.code"), toStr("err.general.notFound.msg"));
        }
    }

    @Override
    public Package findOneByNameAndTenantId(String name, int tenantId) {
        try {
            return packageRepository.findOneByNameAndTenantId(name, tenantId);
        } catch (Exception e) {
            throw new DomainException(toInteger("err.general.notFound.code"), toStr("err.general.notFound.msg"));
        }
    }

    @Override
    public List<Package> findByTenantId(int tenantId) {
        try {
            return packageRepository.findByTenantId(tenantId);
        } catch (Exception e) {
            throw new DomainException(toInteger("err.general.notFound.code"), toStr("err.general.notFound.msg"));
        }
    }

}
