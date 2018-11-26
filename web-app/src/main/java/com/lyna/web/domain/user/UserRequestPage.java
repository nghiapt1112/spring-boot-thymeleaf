package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.repository.QueryBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequestPage extends RequestPage {
    // TODO: => NghiapT check for Singleton if need
    @Override
    public QueryBuilder getDefaultQueryBuilder() {
        return new UserQueryBuilder();
    }

}