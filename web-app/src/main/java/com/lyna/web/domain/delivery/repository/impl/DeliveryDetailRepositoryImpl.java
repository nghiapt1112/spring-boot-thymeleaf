package com.lyna.web.domain.delivery.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import com.lyna.web.domain.stores.repository.impl.StoreRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class DeliveryDetailRepositoryImpl extends BaseRepository<DeliveryDetail, Long> implements DeliveryDetailRepository {

    private final Logger log = LoggerFactory.getLogger(DeliveryDetailRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public DeliveryDetailRepositoryImpl(EntityManager em) {
        super(DeliveryDetail.class, em);
    }

    @Override
    public boolean deletebyPackageId(List<String> listPackageId) {
        try {
            String query = "DELETE FROM DeliveryDetail d WHERE d.packageId in (:listPackageId)";
            entityManager.createQuery(query).setParameter("listPackageId", listPackageId).executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
