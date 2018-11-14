package com.lyna.web.domain.user;

public enum UserType {
    USER(1), ADMIN(2);

    private int val;

    private UserType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

}
