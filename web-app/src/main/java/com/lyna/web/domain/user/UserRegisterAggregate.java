package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.stores.Store;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class UserRegisterAggregate extends AbstractObject {
    private static final String EMAIL_REGEX = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.] {0,1}[a-zA-Z]+";

    @NotEmpty(message = "Lyna-Email can not be empty")
    @Email(regexp = EMAIL_REGEX)
    private String email;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String password;
    private boolean flagTest;
    private String oldData = "";
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

    public static List<UserStoreAuthorityAggregate> toUserStoreAuthorityAggregate(List<Store> stores) {
        return stores.stream()
                .map(UserStoreAuthorityAggregate::fromStoreEntity)
                .collect(Collectors.toList());
    }

}


@Data
@NoArgsConstructor
class UserStoreAuthorityAggregate {
    private String name;
    private String storeId;
    private String storeRole;
    private boolean canView;
    private boolean canEdit;

    public UserStoreAuthority toUserStoreAuthority() {
        UserStoreAuthority userStoreAuthority = new UserStoreAuthority();
        userStoreAuthority.setStoreId(this.storeId);

        if (canEdit) {
            userStoreAuthority.setAuthority(StoreRoleType.EDIT.getShortVal());
        } else if (canView) {
            userStoreAuthority.setAuthority(StoreRoleType.VIEW.getShortVal());
        }

        return userStoreAuthority;
    }

    public static UserStoreAuthorityAggregate fromStoreEntity(Store store) {
        UserStoreAuthorityAggregate aggregate = new UserStoreAuthorityAggregate();
        aggregate.setStoreId(store.getStoreId());
        aggregate.setName(store.getName());
        return aggregate;
    }


}