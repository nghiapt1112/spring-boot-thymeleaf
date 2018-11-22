package com.lyna.web.domain.user;

import com.lyna.web.infrastructure.object.ResponsePage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class UserResponsePage extends ResponsePage<User> {

    public UserResponsePage(int noOfRowInPage, List<User> results, long totalRerords) {
        super(noOfRowInPage, results, totalRerords);
    }
}
