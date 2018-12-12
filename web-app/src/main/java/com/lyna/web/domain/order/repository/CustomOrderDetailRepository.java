package com.lyna.web.domain.order.repository;

import java.util.List;

public interface CustomOrderDetailRepository {
    boolean deleteByProductIds(List<String> productIds);
}
