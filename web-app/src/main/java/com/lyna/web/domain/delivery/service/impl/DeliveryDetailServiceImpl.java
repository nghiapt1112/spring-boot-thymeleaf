package com.lyna.web.domain.delivery.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.service.DeliveryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryDetailServiceImpl extends BaseService implements DeliveryDetailService {
    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;

    @Override
    public boolean deleteByPackageIds(List<String> packageIds) {
        return deliveryDetailRepository.deleteByPackageIds(packageIds);
    }
}
