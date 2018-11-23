package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.infrustructure.object.ResponsePage;
import lombok.Data;

import java.util.List;

@Data
public class UserResponsePage extends ResponsePage {

    public UserResponsePage(int noOfRowInPage, List results, long totalRerords) {
//        super(noOfRowInPage, results, totalRerords);
    }
}
