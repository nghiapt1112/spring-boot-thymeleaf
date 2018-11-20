package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class UserRegisterAggregate extends AbstractObject {
    private String email;
    private String userName;
    private String password;
    private boolean flagTest;
    private String oldData ="";
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

    public UserStoreAuthorityAggregate userStoreRoleInstance() {
        return new UserStoreAuthorityAggregate();
    }

    public List<UserStoreAuthorityAggregate> defaultRolePerStores() {
        List<UserStoreAuthorityAggregate> testData = new ArrayList<>();

        testData.add(new UserStoreAuthorityAggregate("name1", "storeID1", "EDIT"));
        testData.add(new UserStoreAuthorityAggregate("name2","storeID2", "EDIT"));
        testData.add(new UserStoreAuthorityAggregate("name3","storeID3", "EDIT"));
        testData.add(new UserStoreAuthorityAggregate("name4","storeID4", "EDIT"));

        return testData;
    }

    public void getUserPerStore(List<UserStoreAuthorityAggregate> testData) {
        for (UserStoreAuthorityAggregate el : testData) {
            UserStoreAuthorityAggregate userStoreAuth = new UserStoreAuthorityAggregate();
            userStoreAuth.setName(el.getName());
            userStoreAuth.setStoreId(el.getStoreId());
            userStoreAuth.setStoreRole(el.getStoreRole());
            this.updateRolePerStore(userStoreAuth);
        }
    }

    private void updateRolePerStore(UserStoreAuthorityAggregate userStoreAuth) {
        if (CollectionUtils.isEmpty(this.rolePerStore)) {
            this.rolePerStore = new ArrayList<>();
        }

        this.rolePerStore.add(userStoreAuth);

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

    public UserStoreAuthorityAggregate(String name, String storeId, String storeRole) {
        this.name = name;
        this.storeId = storeId;
        this.storeRole = storeRole;
    }

    public UserStoreAuthority toUserStoreAuthority() {
        UserStoreAuthority userStoreAuthority = new UserStoreAuthority();
        userStoreAuthority.setStoreId(this.storeId);
//            userStoreAuthority.setUserId();
//        userStoreAuthority.setTenantId();
        userStoreAuthority.setAuthority(StoreRoleType.shortOf(this.storeRole));
        return userStoreAuthority;
    }
}