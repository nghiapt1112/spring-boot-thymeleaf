package com.lyna.commons.infrustructure.object;

import java.util.List;

public class ResponsePage extends AbstractObject {
//    protected int totalRow;
    protected int totalPage;
    protected int pageNo;
    protected int noOfRowInPage;
    protected List results;

    public ResponsePage() {}

    public ResponsePage(int noOfRowInPage, List results, long totalRerords) {
        this.noOfRowInPage = noOfRowInPage;
        this.results = results;
        this.totalPage = (int) Math.ceil((float) totalRerords / noOfRowInPage);
    }


    public int getTotalPage() {
        return totalPage;
    }

    public int getNoOfRowInPage() {
        return noOfRowInPage;
    }

    public List getResults() {
        return results;
    }
}
