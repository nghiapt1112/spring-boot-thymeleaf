package com.lyna.web.infrastructure.repository;

import com.lyna.web.infrastructure.object.RequestPage;

public abstract class QueryBuilder {
    protected String alias;

    protected RequestPage requestPage;

    public abstract String buildGroupBy();

    public abstract String buildOrderBy();

    public abstract String buildWhere();
}