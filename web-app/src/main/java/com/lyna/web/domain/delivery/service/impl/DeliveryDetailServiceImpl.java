package com.lyna.web.domain.delivery.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.repository.impl.DeliveryDetailRepositoryImpl;
import com.lyna.web.domain.delivery.service.DeliveryDetailService;
import com.lyna.web.domain.logicstics.repository.LogiticDetailRepository;
import com.lyna.web.domain.mpackage.exception.PackageException;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeliveryDetailServiceImpl extends BaseService implements DeliveryDetailService {

    private final Logger log = LoggerFactory.getLogger(DeliveryDetailRepositoryImpl.class);

    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;

    @Autowired
    private LogiticDetailRepository LogiticDetailRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Override
    @Transactional
    public boolean deleteDeliveryDetailByPackageIdsAndTenantId(List<String> packageIds, int tenantId) {
        try {
            deliveryDetailRepository.deleteDeliveryDetailByPackageIdsAndTenantId(packageIds, tenantId);
            LogiticDetailRepository.deleteByPackageIdsAndTenantId(packageIds, tenantId);
            packageRepository.deleteByPackageIdsAndTenantId(packageIds, tenantId);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PackageException(toInteger("err.package.deleteFailed.code"), toStr("err.package.deleteFailed.msg"));
        }
    }
}
