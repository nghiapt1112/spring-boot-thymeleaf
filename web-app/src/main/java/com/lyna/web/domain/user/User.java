package com.lyna.web.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.security.SecurityUtils;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Entity
@Table(name = "m_user")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@NamedQueries({
        @NamedQuery(name = "User.countAll", query = "SELECT COUNT(x) FROM User x")
})
@Data
public class User extends AbstractEntity implements UserDetails {
    private static final String EMAIL_REGEX = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.] {0,1}[a-zA-Z]+";
    @Id
    @Column(name = "user_id", nullable = false)
    private String id;

    @Column
    @NotEmpty(message = "email cannot be empty")
    @Email(regexp = EMAIL_REGEX)
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private short role;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<UserStoreAuthority> userStoreAuthorities;

    @JsonIgnore
    @ManyToMany()
    @JoinTable(name = "m_user_store_authority", joinColumns = {
            @JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "store_id")})
    private Set<Store> stores = new HashSet<>();

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (Objects.equals(Integer.valueOf(this.getRole()), UserType.ADMIN.getVal())) {
            return Arrays.asList(new SimpleGrantedAuthority(SecurityUtils.ROLE_PREFIX.concat(UserType.ADMIN.name())));
        }
        return SecurityUtils.populateGrantedAuthorities(this.userStoreAuthorities);
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void hideSensitiveFields() {
        this.createDate = null;
        this.createUser = null;
        this.updateDate = null;
        this.updateUser = null;
    }

    //TODO: move to AbstractEntity
    public User withDefaultFields(User currentUser) {
        this.tenantId = currentUser.getTenantId();
        this.initDefaultFieldsCreate();
        this.createUser = currentUser.getId();
        return this;
    }

    public Stream<UserStoreAuthority> getStoreAuthoritiesAsStream() {
        return this.userStoreAuthorities.stream();
    }

    public Stream<Store> getStoresAsStream() {
        return this.stores.stream();
    }

    public void updateInfo(User userToUpdate) {
        this.name = userToUpdate.name;
        this.email = userToUpdate.email;
//        this.password = userToUpdate.password; // Later function.
    }
}
