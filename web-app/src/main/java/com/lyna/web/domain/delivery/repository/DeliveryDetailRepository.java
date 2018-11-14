package com.lyna.web.domain.delivery.repository;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.domain.delivery.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, Long> {
}
