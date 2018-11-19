package com.lyna.web.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.security.SecurityUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "m_user")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@NamedQueries({
        @NamedQuery(name = "User.countAll", query = "SELECT COUNT(x) FROM User x")
})
@Data
@NoArgsConstructor
public class User extends AbstractEntity implements UserDetails {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;

    @Column
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
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getRole() {
        return role;
    }

    public void setRole(short role) {
        this.role = role;
    }

    public Set<UserStoreAuthority> getUserStoreAuthorities() {
        return userStoreAuthorities;
    }

    public void setUserStoreAuthorities(Set<UserStoreAuthority> userStoreAuthorities) {
        this.userStoreAuthorities = userStoreAuthorities;
    }
}
