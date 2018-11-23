package com.lyna.commons.infrustructure.repository;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.object.ResponsePage;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.xml.ws.Response;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseRepository<E extends AbstractEntity, ID extends Serializable> extends SimpleJpaRepository<E, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    public BaseRepository(Class domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;

    }


    public <T>  T getEntityInstance(Class<T> type) {
//        Class<T> type = this.getEntityClass();
        T inst = null;
        try {
            inst = type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace(); // TODO hande ex
        } catch (IllegalAccessException e) {
            e.printStackTrace();// TODO hande ex
        }
        return inst;
    }

    public <T> Class<T> getEntityClass() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
        return type;
    }

    public <T extends ResponsePage> T findWithPaging(RequestPage userRequestPage, QueryBuilder queryBuilder) {
        String whereCondition = queryBuilder.buildWhere();


        TypedQuery<E> tQuery = entityManager.createQuery(queryBuilder.buildSelect() + queryBuilder.buildWhere() , this.getEntityClass());

        long totalRerords = this.countTotalRecord(queryBuilder);

        List<E> results = tQuery.getResultList();


        Class<T> clazz = getEntityClass();
//        T val = (T) new ResponsePage(userRequestPage.getNoOfRowInPage(), results, totalRerords);

        return null;
    }

    //    TODO: =>NghiaPT move to common functions.
    private long countTotalRecord(QueryBuilder queryBuilder) {
        TypedQuery<Long> tQuery = entityManager
                .createQuery(queryBuilder.buildCount() + queryBuilder.buildWhere(), Long.class);

        return tQuery.getSingleResult();
    }

}
