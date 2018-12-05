package com.lyna.web.domain.logicstics.repository;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.object.ResponsePage;
import com.lyna.commons.infrustructure.repository.QueryBuilder;
import com.lyna.web.domain.logicstics.Logistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogisticRepository extends JpaRepository<Logistics, String> {
    <T extends ResponsePage> T findWithPaging(RequestPage userRequestPage, QueryBuilder queryBuilder, Class<T> typed);
}
