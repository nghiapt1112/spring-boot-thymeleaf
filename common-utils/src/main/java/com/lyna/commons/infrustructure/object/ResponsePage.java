package com.lyna.commons.infrustructure.object;

import java.util.List;

public abstract class ResponsePage extends AbstractObject {
    protected int totalPage;
    protected int pageNo;
    protected int noOfRowInPage;
    protected List results;

    public ResponsePage() {}

    public void withData(int noOfRowInPage, List rawResults, long totalRerords) {
        this.noOfRowInPage = noOfRowInPage;
        this.results = parseResult(rawResults);
        this.totalPage = (int) Math.ceil((float) totalRerords / noOfRowInPage);
    }

    protected abstract List parseResult(List rawResults);

}
