package com.lyna.web.domain.delivery.service;

import java.util.List;

public interface DeliveryDetailService {
    boolean deleteByPackageIds(List<String> packageIds);
}
