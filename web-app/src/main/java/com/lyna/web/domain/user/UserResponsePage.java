package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.infrustructure.object.ResponsePage;
import lombok.Data;

import java.util.List;

@Data
public class UserResponsePage extends ResponsePage {

    public UserResponsePage(ResponsePage responsePage) {
        this.noOfRowInPage = responsePage.getNoOfRowInPage();
        this.results = responsePage.getResults();
        this.totalPage = responsePage.getTotalPage();
        //this.pageNo = currentPage;
    }

}
