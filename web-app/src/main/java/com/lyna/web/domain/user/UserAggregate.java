package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.stores.Store;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lyna.web.domain.user.UserStoreRole.fromStoreAuthorityEntity;

@Data
@NoArgsConstructor
public class UserAggregate extends AbstractObject {
    private static final String EMAIL_REGEX = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.] {0,1}[a-zA-Z]+";

    private String userId;
    @NotEmpty(message = "Lyna-Email can not be empty")
    @Email
    private String email;

    @NotEmpty
    private String userName;

    private String password;

    private List<UserStoreRole> rolePerStore;

    private short role;

    public User toUser() {
        User user = new User();
        user.setEmail(this.email);
        user.setName(this.userName);
        user.setPassword(this.password);
        user.setRole(this.role);
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
        this.password = user.getPassword();
        this.role = user.getRole();

        Map<String, String> storeNameById = user.getStoresAsStream()
                .collect(Collectors.toMap(Store::getStoreId, store -> store.getName(), (o1, o2) -> o1));

        this.rolePerStore = user.getStoreAuthoritiesAsStream()
                .map(el -> fromStoreAuthorityEntity(el, storeNameById))
                .collect(Collectors.toList());
        this.rolePerStore.sort(Comparator.comparing(UserStoreRole::getName));

        return this;
    }

    /**
     * Parse List< Store> to List< Aggregate>
     * Cause by entity data will be difference with data in view screen => need to convert.
     */
    public void updateRolePerStore(List<Store> stores) {
        if (CollectionUtils.isEmpty(this.rolePerStore)) {
            this.rolePerStore = new ArrayList<>();
            this.rolePerStore = stores.stream().map(UserStoreRole::fromStoreEntity).collect(Collectors.toList());
        } else {
            Set<String> uniqueExistedStoreIds = this.rolePerStore.stream().map(el -> el.getStoreId()).collect(Collectors.toSet());

            //
            stores.stream()
                    .filter(store -> !uniqueExistedStoreIds.contains(store.getStoreId()))
                    .forEach(store -> {
                        // assign user to new store with NO_PERMISSION role in Store.
                        this.rolePerStore.add(new UserStoreRole(store));
                    });

        }

    }

    public String getName() {
        return this.userName;
    }

    public boolean isRolePerStoreValid(){
        return CollectionUtils.isEmpty(this.rolePerStore);
    }
}


@Data
@NoArgsConstructor
class UserStoreRole extends AbstractObject {
    private String id;
    private String name;
    private String storeId;
    private boolean canView;
    private boolean canEdit;

    public UserStoreRole(Store store) {
        this.setId(UUID.randomUUID().toString());
        this.setStoreId(store.getStoreId());
        this.setName(store.getName());
        this.parseRole(StoreRoleType.NO_PERMISSION.getShortVal());
    }

    public static UserStoreRole fromStoreEntity(Store store) {
        UserStoreRole aggregate = new UserStoreRole();
        aggregate.setStoreId(store.getStoreId());
        aggregate.setName(store.getName());
        return aggregate;
    }

    public static UserStoreRole fromStoreAuthorityEntity(UserStoreAuthority entity, Map<String, String> storeNameById) {
        UserStoreRole aggregate = new UserStoreRole();
        aggregate.setId(entity.getId());
        aggregate.setStoreId(entity.getStoreId());
        aggregate.setName(storeNameById.get(entity.getStoreId()));
        aggregate.parseRole(entity.getAuthority());
        return aggregate;
    }

    public UserStoreAuthority toUserStoreAuthority() {
        UserStoreAuthority userStoreAuthority = new UserStoreAuthority();
        if (!StringUtils.isEmpty(this.id)) {
            userStoreAuthority.setId(this.id);
        }
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

    public String getStoreRole() {
        if (this.canEdit) {
            return "Edit";
        } else if (this.canView) {
            return "View";
        }
        return "NO Permission";
    }
}