package com.lyna.web.domain.user;

import java.util.Map;
import java.util.Objects;

public class UserList {
    Map<String, Integer> mapStore;
    private String userId;
    private String email;
    private String name;
    private short role;

    public Map<String, Integer> getMapStore() {
        return mapStore;
    }

    public void setMapStore(Map<String, Integer> mapStore) {
        this.mapStore = mapStore;
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

    @Override
    public String toString() {
        return "UserList{" +
                "mapStore=" + mapStore +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserList)) return false;
        UserList userList = (UserList) o;
        return getRole() == userList.getRole() &&
                Objects.equals(getMapStore(), userList.getMapStore()) &&
                Objects.equals(getUserId(), userList.getUserId()) &&
                Objects.equals(getEmail(), userList.getEmail()) &&
                Objects.equals(getName(), userList.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMapStore(), getUserId(), getEmail(), getName(), getRole());
    }
}
