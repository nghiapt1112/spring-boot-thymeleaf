package com.lyna.web.domain.view;

public enum MainMenuDataType {
    LOGISTIC(0),
    DELIVERY_OVERRIDE(1), // delivery has same orderId with logistic
    DELIVERY_ORIGINAL(2);

    private int value;

    MainMenuDataType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
