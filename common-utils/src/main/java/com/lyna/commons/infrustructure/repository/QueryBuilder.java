package com.lyna.commons.infrustructure.repository;

import com.lyna.commons.infrustructure.object.RequestPage;

import java.util.HashMap;
import java.util.Map;

public abstract class QueryBuilder {
    protected static final String EMPTY_STR = "";
    protected RequestPage requestPage;
    protected Map<String, Object> params = new HashMap<>();

    public QueryBuilder withRequestPage(RequestPage requestPage) {
        this.requestPage = requestPage;
        return this;
    }

    public abstract String buildGroupBy();

    public abstract String buildOrderBy();

    public abstract String buildWhere();

    public abstract String buildSelect();

    public abstract String buildCount();

    public abstract String buildLimit();

    public abstract Map<String, Object> getParams();
}