package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.ResponsePage;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class UserResponsePage extends ResponsePage {

//    public UserResponsePage(ResponsePage responsePage) {
//        this.noOfRowInPage = responsePage.getNoOfRowInPage();
//        this.results = responsePage.getResults();
//        this.totalPage = responsePage.getTotalPage();
//        //this.pageNo = currentPage;
//    }


    @Override
    protected List parseResult(List rawResults) {
        // TODO: parse rawResults to FE view
        System.out.println("\n\n\n=================================================================\n\n\n");
        return rawResults;
    }
}
