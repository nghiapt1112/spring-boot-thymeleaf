package com.lyna.web.infrastructure.repository;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.object.ResponsePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;

public class BaseRepository<E extends AbstractObject, ID extends Serializable> extends SimpleJpaRepository<E, ID> implements PagingRepository {
    private static final int NO_RECORDS = -1;

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @PersistenceContext
    protected EntityManager entityManager;

    public BaseRepository(Class domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
    }

    public Class<E> getEntityClass() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<E> type = (Class<E>) superClass.getActualTypeArguments()[0];
        return type;
    }

    @Override
    public <T extends ResponsePage, O extends AbstractObject> T findWithPaging(RequestPage requestPage, Class<T> typedResponse, Class<O> typedQuery) {
        Query tQuery = entityManager.createNativeQuery(requestPage.getFullQuery(), typedQuery);
        return getResponsePage(requestPage, typedResponse, tQuery);
    }

    @Override
    public <T extends ResponsePage> T findWithPaging(RequestPage requestPage, Class<T> typedResponse, String resultSetMapping) {
        Query tQuery = entityManager.createNativeQuery(requestPage.getFullQuery(), resultSetMapping);
        return getResponsePage(requestPage, typedResponse, tQuery);
    }

    private <T extends ResponsePage> T getResponsePage(RequestPage requestPage, Class<T> typedResponse, Query tQuery) {
        fillParams(tQuery, requestPage.getParams());
        try {
            T responsePageInstance = typedResponse.newInstance();
            responsePageInstance.withData(requestPage.getNoOfRowInPage(), tQuery.getResultList(), this.countTotalRecord(requestPage));
            return responsePageInstance;
        } catch (InstantiationException | IllegalAccessException | RuntimeException e) {
//            LOGGER.error("Find paging for {}, failed with message: {}, cause: {}", typed, e.getMessage(), e.getCause());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * TODO: using countTotalRecord(String nativeQuery, Map<String, Object> params) for new version.
     *
     * @param queryBuilder
     * @return
     */
    @Deprecated
    protected long countTotalRecord(RequestPage queryBuilder) {
        Query nativeQuery = entityManager
                .createNativeQuery(queryBuilder.buildCount().append(queryBuilder.buildWhere()).toString());
        fillParams(nativeQuery, queryBuilder.getParams());
        Object singResult = nativeQuery.getSingleResult();
        if (Objects.isNull(singResult)) {
            return NO_RECORDS;
        }
        return ((BigInteger) singResult).intValue();
    }

    @Override
    public long countTotalRecord(String nativeQuery, Map<String, Object> params) {
        Query jpaQuery = entityManager.createNativeQuery(nativeQuery);
        fillParams(jpaQuery, params);
        Object singResult = jpaQuery.getSingleResult();
        if (Objects.isNull(singResult)) {
            return NO_RECORDS;
        }
        return ((BigInteger) singResult).intValue();
    }
}
