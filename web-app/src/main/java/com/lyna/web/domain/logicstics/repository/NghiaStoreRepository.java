package com.lyna.web.domain.logicstics.repository;

import com.lyna.web.domain.stores.Store;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NghiaStoreRepository extends JpaRepository<Store, String>, PagingRepository {
//    <T extends ResponsePage> T findWithPaging(RequestPage userRequestPage, QueryBuilder queryBuilder, Class<T> typed);
//
//    <T extends ResponsePage, O extends AbstractObject> T findWithPaging(RequestPage requestPage, QueryBuilder queryBuilder, Class<T> typed, Class<O> typedQuery);
}
