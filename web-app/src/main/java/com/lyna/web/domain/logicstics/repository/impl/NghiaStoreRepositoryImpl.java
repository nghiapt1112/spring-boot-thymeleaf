package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.object.ResponsePage;
import com.lyna.commons.infrustructure.repository.QueryBuilder;
import com.lyna.web.domain.logicstics.repository.NghiaStoreRepository;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.infrastructure.repository.BaseRepository;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Objects;

@Repository
public class NghiaStoreRepositoryImpl extends BaseRepository<Store, String> implements NghiaStoreRepository, PagingRepository {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public NghiaStoreRepositoryImpl(EntityManager em) {
        super(Store.class, em);
    }

//
//    public <T extends ResponsePage, O extends AbstractObject> T findWithPaging(RequestPage requestPage, QueryBuilder queryBuilder, Class<T> typed, Class<O> typedQuery) {
//        LOGGER.info("findWithPaging");
////        Class<O> queryTyped = this.getEntityClass();
////        if (Objects.nonNull(typedQuery)) {
////            queryTyped = typedQuery;
////        }
//
//        Query tQuery = entityManager.createNativeQuery(
//                queryBuilder.buildSelect()
//                        .concat(queryBuilder.buildWhere())
//                        .concat(queryBuilder.buildGroupBy())
//                        .concat(queryBuilder.buildOrderBy())
//                        .concat(queryBuilder.buildLimit()),
//                Store.MAIN_MENU_STORE_ORDER_LIST);
//
//        fillParams(tQuery, queryBuilder.getParams());
//
//        try {
//            LOGGER.info("excuting query");
//            T responsePageInstance = typed.newInstance();
//            responsePageInstance.withData(requestPage.getNoOfRowInPage(), tQuery.getResultList(), super.countTotalRecord(queryBuilder));
//            return responsePageInstance;
//        } catch (InstantiationException | IllegalAccessException | RuntimeException e) {
//            e.printStackTrace(); // TODO: log error instead of print stacktrace.
//            return null;
//        }
//    }
//
//    public <T extends ResponsePage> T findWithPaging(RequestPage requestPage, QueryBuilder queryBuilder, Class<T> typed) {
//        return this.findWithPaging(requestPage, queryBuilder, typed, null);
//    }
}
