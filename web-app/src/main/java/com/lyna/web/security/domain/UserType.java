package com.lyna.web.security.domain;

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
