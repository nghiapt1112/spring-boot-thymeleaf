package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.stores.Store;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lyna.web.domain.user.UserStoreRole.fromStoreAuthorityEntity;

@Data
@NoArgsConstructor
public class UserAggregate extends AbstractObject {
    private static final String EMAIL_REGEX = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.] {0,1}[a-zA-Z]+";

    private String userId;
    @NotEmpty(message = "Lyna-Email can not be empty")
    @Email(regexp = EMAIL_REGEX)
    private String email;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String password;

    private List<UserStoreRole> rolePerStore;

    public User toUser() {
        User user = new User();
        user.setEmail(this.email);
        user.setName(this.userName);
        user.setPassword(this.password);
        return user;
    }

    /**
     * Parse Aggregate to List< UserStoreAuthority>
     */
    public Stream<UserStoreAuthority> toUserStoreAuthorities() {
        return this.rolePerStore.stream()
                .map(UserStoreRole::toUserStoreAuthority);
    }

    public UserAggregate fromUserEntity(User user) {
        this.email = user.getEmail();
        this.userName = user.getName();
        this.userId = user.getId();

        Map<String, String> storeNameById = user.getStoresAsStream()
                .collect(Collectors.toMap(Store::getStoreId, store -> store.getName(), (o1, o2) -> o1));

        this.rolePerStore = user.getStoreAuthoritiesAsStream()
                .map(el -> fromStoreAuthorityEntity(el, storeNameById))
                .collect(Collectors.toList());

        return this;
    }

    /**
     * Parse List< Store> to List< Aggregate>
     * Cause by entity data will be difference with data in view screen => need to convert.
     */
    public void updateRolePerStore(List<Store> stores) {
        if (CollectionUtils.isEmpty(this.rolePerStore)) {
            this.rolePerStore = new ArrayList<>();
        }
        this.rolePerStore = stores.stream().map(UserStoreRole::fromStoreEntity).collect(Collectors.toList());
    }
}


@Data
@NoArgsConstructor
class UserStoreRole {
    private String name;
    private String storeId;
    private boolean canView;
    private boolean canEdit;

    public static UserStoreRole fromStoreEntity(Store store) {
        UserStoreRole aggregate = new UserStoreRole();
        aggregate.setStoreId(store.getStoreId());
        aggregate.setName(store.getName());
        return aggregate;
    }

    public static UserStoreRole fromStoreAuthorityEntity(UserStoreAuthority entity, Map<String, String> storeNameById) {
        UserStoreRole aggregate = new UserStoreRole();
        aggregate.setStoreId(entity.getStoreId());
        aggregate.setName(storeNameById.get(entity.getStoreId()));
        aggregate.parseRole(entity.getAuthority());
        return aggregate;
    }

    public UserStoreAuthority toUserStoreAuthority() {
        UserStoreAuthority userStoreAuthority = new UserStoreAuthority();
        userStoreAuthority.setStoreId(this.storeId);

        if (canEdit) {
            userStoreAuthority.setAuthority(StoreRoleType.EDIT.getShortVal());
        } else if (canView) {
            userStoreAuthority.setAuthority(StoreRoleType.VIEW.getShortVal());
        } else if (this.isUserNoPermissionForAllStore()) {
            userStoreAuthority.setAuthority(StoreRoleType.NO_PERMISSION.getShortVal());
        }

        return userStoreAuthority;
    }

    public boolean isUserNoPermissionForAllStore() {
        return !this.canEdit && !this.canView;
    }

    public void parseRole(short authority) {
        this.canView = false;
        this.canEdit = false;
        if (authority == 1) {
            this.canEdit = true;
        } else if (authority == 0) {
            this.canView = true;
        }
    }
}