package com.lyna.commons.infrustructure.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Objects;

public abstract class BaseService {
    @Autowired
    protected Environment env;


    public String toStr(String p) {
        String val = env.getProperty(p);
        return StringUtils.isEmpty(val) ? p : val;
    }

    public Integer toInteger(String p) {
        Integer val = env.getProperty(p, Integer.class);
        return Objects.isNull(val) ? -1 : val;
    }
}
