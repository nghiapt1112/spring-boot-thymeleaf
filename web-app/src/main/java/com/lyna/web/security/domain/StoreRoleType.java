package com.lyna.web.security.domain;

import java.util.HashMap;
import java.util.Map;

public enum StoreRoleType {
    VIEW(1), EDIT(2);

    private static final Map<Integer, StoreRoleType> storeRoleByValue = new HashMap<>();

    static {
        for(StoreRoleType el : StoreRoleType.values()) {
            storeRoleByValue.put(el.getVal(), el);
        }
    }
    private int val;

    StoreRoleType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static StoreRoleType fromVal(int val) {
        return storeRoleByValue.get(val);
    }
}
