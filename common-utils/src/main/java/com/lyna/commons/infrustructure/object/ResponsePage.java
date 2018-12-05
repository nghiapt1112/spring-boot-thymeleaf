package com.lyna.commons.infrustructure.object;

import java.util.List;

public abstract class ResponsePage<E extends AbstractEntity, O extends AbstractObject> extends AbstractObject {
    protected int totalPage;
    protected int pageNo;
    protected int noOfRowInPage;
    protected long totalRecords;
    protected List<O> results;

    public ResponsePage() {}

    public void withData(int noOfRowInPage, List<E> rawResults, long totalRecords) {
        this.noOfRowInPage = noOfRowInPage;
        this.results = parseResult(rawResults);
        this.totalRecords = totalRecords;
        this.totalPage = (int) Math.ceil((float) totalRecords / noOfRowInPage);
    }

    protected abstract List<O> parseResult(List<E> rawResults);

    public int getTotalPage() {
        return totalPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getNoOfRowInPage() {
        return noOfRowInPage;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public List<O> getResults() {
        return this.results;
    }
}
