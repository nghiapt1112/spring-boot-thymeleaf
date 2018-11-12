package com.lyna.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyna.commons.infrustructure.object.AbstractObject;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Roles extends AbstractObject implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "role_description", nullable = true)
    private String roleDescription;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();


    public Roles() {
    }

    public Roles(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName == null ? "" : roleName;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return roleName;
    }

    @JsonIgnore
    public String getRoleNameUpperCase() {
        return this.getRoleName().toUpperCase();
    }

}
