package com.lyna.web.domain.order.service;

import java.util.List;

public interface OrderDetailService {
    boolean deletebyProductId(List<String> listProductId);
}
