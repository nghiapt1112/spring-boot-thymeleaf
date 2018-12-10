package com.lyna.web.domain.delivery.repository.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class DeliveryDetailRepositoryImpl extends BaseRepository<DeliveryDetail, Long> implements DeliveryDetailRepository {

    private final Logger log = LoggerFactory.getLogger(DeliveryDetailRepositoryImpl.class);

    public DeliveryDetailRepositoryImpl(EntityManager em) {
        super(DeliveryDetail.class, em);
    }

    @Override
    public boolean deleteByPackageIds(List<String> packageIds) throws DomainException {
        try {
            String query = "DELETE FROM DeliveryDetail d WHERE d.packageId in (:listPackageId)";
            entityManager.createQuery(query).setParameter("listPackageId", packageIds).executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
