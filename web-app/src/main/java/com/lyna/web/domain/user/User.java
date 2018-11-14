package com.lyna.web.domain.user;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;


@Entity
@Table(name = "m_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "User.countAll", query = "SELECT COUNT(x) FROM User x")
})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id", nullable = false, length = 36)
    public String userId;


    @Basic
    @Column(name = "email", nullable = true, length = 255)
    public String email;


    @Basic
    @Column(name = "password", nullable = true, length = 50)
    public String password;


    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String name;


    @Basic
    @Column(name = "role", nullable = true)
    public Short role;


    @Basic
    @Column(name = "created_date", nullable = true)
    public Timestamp createdDate;


    @Basic
    @Column(name = "create_user", nullable = true, length = 36)
    public String createUser;


    @Basic
    @Column(name = "update_date", nullable = true)
    public Timestamp updateDate;


    @Basic
    @Column(name = "update_user", nullable = true, length = 36)
    public String updateUser;

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

    public Short getRole() {
        return role;
    }

    public void setRole(Short role) {
        this.role = role;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", createdDate=" + createdDate +
                ", createUser='" + createUser + '\'' +
                ", updateDate=" + updateDate +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUserId(), user.getUserId()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getRole(), user.getRole()) &&
                Objects.equals(getCreatedDate(), user.getCreatedDate()) &&
                Objects.equals(getCreateUser(), user.getCreateUser()) &&
                Objects.equals(getUpdateDate(), user.getUpdateDate()) &&
                Objects.equals(getUpdateUser(), user.getUpdateUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmail(), getPassword(), getName(), getRole(), getCreatedDate(), getCreateUser(), getUpdateDate(), getUpdateUser());
    }
}
