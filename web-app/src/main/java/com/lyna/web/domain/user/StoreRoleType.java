package com.lyna.web.domain.user;

import java.util.HashMap;
import java.util.Map;

public enum StoreRoleType {
    VIEW(0), EDIT(1);

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

    public static short shortOf(String value) {
        return (short) StoreRoleType.valueOf(value).getVal();
    }

    public static int intOf(String val) {
        return StoreRoleType.valueOf(val).getVal();
    }

}
