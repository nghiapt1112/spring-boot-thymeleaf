package com.lyna.web.domain.delivery.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.service.DeliveryDetailService;
import com.lyna.web.domain.logicstics.repository.LogiticDetailRepository;
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
    private LogiticDetailRepository LogiticDetailRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Override
    @Transactional
    public boolean deleteByPackageIds(List<String> packageIds) {
        deliveryDetailRepository.deleteByPackageIds(packageIds);
        LogiticDetailRepository.deleteByPackageIds(packageIds);
        packageRepository.deleteByPackageIds(packageIds);
        return true;

    }
}
