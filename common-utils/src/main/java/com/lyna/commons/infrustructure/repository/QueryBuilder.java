package com.lyna.commons.infrustructure.repository;

import com.lyna.commons.infrustructure.object.RequestPage;

import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {

    protected RequestPage requestPage;
    protected Map<String, Object> params = new HashMap<>();

    public QueryBuilder withRequestPage(RequestPage requestPage) {
        this.requestPage = requestPage;
        return this;
    }

    public  String buildGroupBy(){
        return "";
    }

    public  String buildOrderBy(){
        return "";
    }

    public String buildWhere(){
        return "";
    }

    public String buildSelect() {
        return "";
    }

    public String buildCount() {
        return "";
    }
    public String buildLimit() {
        return "";
    }

    public Map<String, Object> getParams() {
        return params;
    }
}