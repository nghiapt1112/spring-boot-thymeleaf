package com.lyna.web.infrastructure.object;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponsePage<T extends AbstractObject> extends AbstractObject {
    protected int totalRow;
    protected int totalPage;
    protected int pageNo;
    protected int noOfRowInPage;
    protected List<T> results;

    public ResponsePage(int noOfRowInPage, List<T> results, long totalRerords) {
        this.noOfRowInPage = noOfRowInPage;
        this.results = results;
        this.totalPage = (int) Math.ceil((float) totalRerords/noOfRowInPage);
    }
}
