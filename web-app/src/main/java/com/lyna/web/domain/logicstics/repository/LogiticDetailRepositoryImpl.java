package com.lyna.web.domain.logicstics.repository;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class LogiticDetailRepositoryImpl extends BaseRepository<LogiticsDetail, Long> implements LogiticDetailRepository {

    public LogiticDetailRepositoryImpl(EntityManager em) {
        super(LogiticsDetail.class, em);
    }

    @Override
    public boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId) throws DomainException {
        String query = "DELETE FROM LogiticsDetail l WHERE l.packageId in (:packageIds) AND l.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("packageIds", packageIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
        return true;
    }
}
