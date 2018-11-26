package com.lyna.commons.infrustructure.repository;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.object.ResponsePage;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public class BaseRepository<E extends AbstractEntity, ID extends Serializable> extends SimpleJpaRepository<E, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    public BaseRepository(Class domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;

    }

    public <T> Class<T> getEntityClass() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
        return type;
    }

    public <T extends ResponsePage> T findWithPaging(RequestPage userRequestPage, QueryBuilder queryBuilder, Class<T> typed) {
        TypedQuery<E> tQuery = entityManager.createQuery(queryBuilder.buildSelect().concat(queryBuilder.buildWhere()), this.getEntityClass());
        try {
            T responsePageInstance = typed.newInstance();
            responsePageInstance.withData(userRequestPage.getNoOfRowInPage(), tQuery.getResultList(), this.countTotalRecord(queryBuilder));
            return responsePageInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private long countTotalRecord(QueryBuilder queryBuilder) {
        return entityManager
                .createQuery(queryBuilder.buildCount().concat(queryBuilder.buildWhere()), Long.class)
                .getSingleResult();
    }

}
