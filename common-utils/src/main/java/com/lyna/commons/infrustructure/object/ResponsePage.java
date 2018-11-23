package com.lyna.commons.infrustructure.object;

import java.util.List;

public class ResponsePage extends AbstractObject {
    protected int totalRow;
    protected int totalPage;
    protected int pageNo;
    protected int noOfRowInPage;
    protected List results;

    public ResponsePage withResult(int noOfRowInPage, List results, long totalRerords) {
        this.noOfRowInPage = noOfRowInPage;
        this.results = results;
        this.totalPage = (int) Math.ceil((float) totalRerords / noOfRowInPage);
        return this;
    }
}
