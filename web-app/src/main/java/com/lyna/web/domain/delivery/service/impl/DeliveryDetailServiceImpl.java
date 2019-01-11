package com.lyna.web.domain.delivery.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.service.DeliveryDetailService;
import com.lyna.web.domain.logicstics.repository.LogisticDetailRepository;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeliveryDetailServiceImpl extends BaseService implements DeliveryDetailService {

    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;

    @Autowired
    private LogisticDetailRepository logisticDetailRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Override
    @Transactional
    public boolean deleteDeliveryDetailByPackageIdsAndTenantId(List<String> packageIds, int tenantId) {
        try {
            deliveryDetailRepository.deleteDeliveryDetailByPackageIdsAndTenantId(packageIds, tenantId);
            logisticDetailRepository.deleteByPackageIdsAndTenantId(packageIds, tenantId);
            packageRepository.deleteByPackageIdsAndTenantId(packageIds, tenantId);
            return true;
        } catch (Exception e) {
            throw new DomainException(toInteger("err.general.deleteFailed.code"), toStr("err.general.deleteFailed.msg"));
        }
    }
}
