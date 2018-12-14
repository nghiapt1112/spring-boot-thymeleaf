package com.lyna.commons.infrustructure.object;

import java.util.HashMap;
import java.util.Map;

public abstract class RequestPage extends AbstractObject {
    protected int tenantId;
    protected int noOfRowInPage;
    protected int currentPage;
    protected Map<String, Object> sortFields;
    protected Map<String, Object> searchFields;

    {
        sortFields = new HashMap<>();
        searchFields = new HashMap<>();
    }

    public int getNoOfRowInPage() {
        return noOfRowInPage;
    }

    public void setSortFields(Map<String, Object> sortFields) {
        this.sortFields = sortFields;
    }

    public void setSearchFields(Map<String, Object> searchFields) {
        this.searchFields = searchFields;
    }


    public void setNoOfRowInPage(int noOfRowInPage) {
        this.noOfRowInPage = noOfRowInPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Map<String, Object> getSortFields() {
        return sortFields;
    }

    public Map<String, Object> getSearchFields() {
        return searchFields;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public RequestPage addSearchField(String key, Object value) {
        this.searchFields.put(key, value);
        return this;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public RequestPage withTenantId(int tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Query builder
     */
    protected static final StringBuilder EMPTY_STR = new StringBuilder();
    protected Map<String, Object> params = new HashMap<>();

    public abstract StringBuilder buildGroupBy();

    public abstract StringBuilder buildOrderBy();

    public abstract StringBuilder buildWhere();

    public abstract StringBuilder buildSelect();

    public abstract StringBuilder buildCount();

    public abstract StringBuilder buildLimit();

    public abstract StringBuilder buildFrom();

    public Map<String, Object> getParams() {
        return params;
    }

    public String getFullQuery() {
        return this.buildSelect()
                .append(this.buildFrom())
                .append(this.buildWhere())
                .append(this.buildGroupBy())
                .append(this.buildOrderBy())
                .append(this.buildLimit()).toString();
    }

}
