package com.lyna.commons.infrustructure.object;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class AbstractObject implements Serializable, Cloneable {
    private static final String[] EXCLUDED_FIELD_NAMES_FROM_TOSTRING = new String[]{"password"};

    public AbstractObject() {
    }

    public String toString() {
        return (new ReflectionToStringBuilder(this))
                .setExcludeFieldNames(EXCLUDED_FIELD_NAMES_FROM_TOSTRING)
                .toString();
    }

}
