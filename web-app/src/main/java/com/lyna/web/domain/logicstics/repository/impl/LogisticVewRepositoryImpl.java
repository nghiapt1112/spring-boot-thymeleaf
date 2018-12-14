package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.domain.logicstics.DeliveryView;
import com.lyna.web.domain.logicstics.LogisticView;
import com.lyna.web.domain.logicstics.repository.LogisticViewRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class LogisticVewRepositoryImpl extends BaseRepository<LogisticView, String> implements LogisticViewRepository {

    public LogisticVewRepositoryImpl(EntityManager em) {
        super(LogisticView.class, em);
    }

    @Override
    public List<LogisticView> findLogistics(int tenantId, RequestPage logisticRequestPage) {
        String sqlQuery = logisticRequestPage.buildSelect()
                .append(logisticRequestPage.buildFrom())
                .append(logisticRequestPage.buildWhere())
                .toString();

        TypedQuery<LogisticView> jpqlQuery = entityManager.createQuery(sqlQuery, LogisticView.class);

        fillParams(jpqlQuery, logisticRequestPage.getParams());

        return jpqlQuery.getResultList();
    }

    @Override
    public List<DeliveryView> findDeliveries(int tenantId, RequestPage requestPage) {
        String sqlQuery = requestPage.buildSelect()
                .append(" FROM DeliveryView v ")
                .append(requestPage.buildWhere())
                .toString();

        TypedQuery<DeliveryView> jpqlQuery = entityManager.createQuery(sqlQuery, DeliveryView.class);

        fillParams(jpqlQuery, requestPage.getParams());

        return jpqlQuery.getResultList();
    }

}
