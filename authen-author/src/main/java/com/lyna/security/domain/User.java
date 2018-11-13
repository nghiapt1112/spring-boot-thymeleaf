package com.lyna.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lyna.commons.infrustructure.object.AbstractEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "m_user")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
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
    private short authority;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name="user_id")
    private Set<UserStoreAuthority> userStoreAuthorities;

    public User() {
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
//        return SecurityUtils.generateGrantedAuthorities(this.getRoles());
        return Collections.emptyList();
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
        return this.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
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

    @Override
    public String getPassword() {
        return password;
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

    public short getAuthority() {
        return authority;
    }

    public void setAuthority(short authority) {
        this.authority = authority;
    }

    public Set<UserStoreAuthority> getUserStoreAuthorities() {
        return userStoreAuthorities;
    }

    public void setUserStoreAuthorities(Set<UserStoreAuthority> userStoreAuthorities) {
        this.userStoreAuthorities = userStoreAuthorities;
    }
}
