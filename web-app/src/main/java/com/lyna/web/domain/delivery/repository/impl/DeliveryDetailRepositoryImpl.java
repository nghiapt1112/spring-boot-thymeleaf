package com.lyna.web.domain.delivery.repository.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class DeliveryDetailRepositoryImpl extends BaseRepository<DeliveryDetail, Long> implements DeliveryDetailRepository {

    private final Logger log = LoggerFactory.getLogger(DeliveryDetailRepositoryImpl.class);

    public DeliveryDetailRepositoryImpl(EntityManager em) {
        super(DeliveryDetail.class, em);
    }

    @Override
    public boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId) throws DomainException {
        String query = "DELETE FROM DeliveryDetail d WHERE d.packageId in (:packageIds) AND d.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("packageIds", packageIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
        return true;

    }
}
