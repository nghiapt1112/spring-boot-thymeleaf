package com.lyna.web.domain.logicstics.repository;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import com.lyna.web.domain.mpackage.Package;
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
public class LogiticDetailRepositoryImpl extends BaseRepository<Package, Long> implements LogiticDetailRepository {
    private final Logger log = LoggerFactory.getLogger(StoreRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public LogiticDetailRepositoryImpl(EntityManager em) {
        super(LogiticsDetail.class, em);
    }

    @Override
    public boolean deletebyPackageId(List<String> listPackageId) {
        try {
            String query = "DELETE FROM LogiticsDetail l WHERE l.packageId in (:listPackageId)";
            entityManager.createQuery(query).setParameter("listPackageId", listPackageId).executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
