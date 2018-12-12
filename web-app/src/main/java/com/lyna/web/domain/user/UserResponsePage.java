package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.ResponsePage;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class UserResponsePage extends ResponsePage<User, UserAggregate> {

    @Override
    protected List<UserAggregate> parseResult(List<User> rawResults) {
        return rawResults.stream()
                .map(el -> new UserAggregate().fromUserEntity(el))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAggregate> getResults() {
        return this.results;
    }

}
