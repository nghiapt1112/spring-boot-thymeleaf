package com.lyna.web.infrastructure.repository;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.object.ResponsePage;
import org.apache.commons.collections4.MapUtils;

import javax.persistence.Query;
import java.util.Map;

public interface PagingRepository {
    default <T extends ResponsePage> T findWithPaging(RequestPage requestPage, Class<T> typed) {
        return this.findWithPaging(requestPage, typed, getEntityClass());
    }

    <T extends ResponsePage, O extends AbstractObject> T findWithPaging(RequestPage requestPage, Class<T> typedResponse, Class<O> typedQuery);

    <T extends ResponsePage> T findWithPaging(RequestPage requestPage, Class<T> typedResponse, String resultSetMapping);

    <T> Class<T> getEntityClass();

    long countTotalRecord(String nativeQuery, Map<String, Object> params);

    default void fillParams(Query query, Map<String, Object> params) {
        if (MapUtils.isEmpty(params)) {
            return;
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

}
