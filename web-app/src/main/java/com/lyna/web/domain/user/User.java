package com.lyna.web.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.security.SecurityUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotEmpty(message = "Nghia dep trai, email khong the null dau")
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private short role;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name="user_id")
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

}
