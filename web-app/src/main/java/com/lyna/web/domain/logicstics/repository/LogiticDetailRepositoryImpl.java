package com.lyna.web.domain.logicstics.repository;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import com.lyna.web.domain.stores.repository.impl.StoreRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class LogiticDetailRepositoryImpl extends BaseRepository<LogiticsDetail, Long> implements LogiticDetailRepository {
    private final Logger log = LoggerFactory.getLogger(StoreRepositoryImpl.class);

    public LogiticDetailRepositoryImpl(EntityManager em) {
        super(LogiticsDetail.class, em);
    }

    @Override
    public boolean deleteByPackageIds(List<String> packageIds) throws DomainException {
        try {
            String query = "DELETE FROM LogiticsDetail l WHERE l.packageId in (:packageIds)";
            entityManager.createQuery(query).setParameter("packageIds", packageIds).executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
