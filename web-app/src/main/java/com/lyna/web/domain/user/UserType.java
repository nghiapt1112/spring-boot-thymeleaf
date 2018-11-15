package com.lyna.web.domain.user;

public enum UserType {
    GENERAL_USER(0), ADMIN(1);

    private int val;

    private UserType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

}
