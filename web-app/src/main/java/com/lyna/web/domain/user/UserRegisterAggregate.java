package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class UserRegisterAggregate extends AbstractObject {
    private String email;
    private String userName;
    private String password;
    private List<UserStoreAuthorityAggregate> rolePerStore;

    public User toUser() {
        User user = new User();
        user.setEmail(this.email);
        user.setName(this.userName);
        user.setPassword(this.password);
        return user;
    }

    public Stream<UserStoreAuthority> toUserStoreAuthorities() {
        return this.rolePerStore.stream()
                .map(UserStoreAuthorityAggregate::toUserStoreAuthority);
    }

}


@Data
@NoArgsConstructor
class UserStoreAuthorityAggregate {
    private String storeId;
    private String storeRole;

    public UserStoreAuthority toUserStoreAuthority() {
        UserStoreAuthority userStoreAuthority = new UserStoreAuthority();
        userStoreAuthority.setStoreId(this.storeId);
//            userStoreAuthority.setUserId();
//        userStoreAuthority.setTenantId();
        userStoreAuthority.setAuthority(StoreRoleType.shortOf(this.storeRole));
        return userStoreAuthority;
    }
}