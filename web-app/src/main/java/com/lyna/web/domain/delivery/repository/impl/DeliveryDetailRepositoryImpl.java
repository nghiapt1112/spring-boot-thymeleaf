package com.lyna.web.domain.delivery.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class DeliveryDetailRepositoryImpl extends BaseRepository<DeliveryDetail, Long> implements DeliveryDetailRepository {

    public DeliveryDetailRepositoryImpl(EntityManager em) {
        super(DeliveryDetail.class, em);
    }

}
