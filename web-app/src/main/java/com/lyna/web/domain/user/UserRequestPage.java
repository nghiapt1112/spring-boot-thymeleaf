package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.repository.QueryBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class UserRequestPage extends RequestPage {
    public static final List<String> SORT_FIELDS = Arrays.asList("NAME", "MAIL");


    // TODO: => NghiapT check for Singleton if need
    @Override
    public QueryBuilder getDefaultQueryBuilder() {
        return new UserQueryBuilder();
    }

}
