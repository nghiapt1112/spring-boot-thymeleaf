package com.lyna.commons.infrustructure.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class BaseService {
    @Autowired
    protected Environment env;
    protected Map<Integer, String> mapError;
    @Autowired
    private MessageSource messageSource;

    public String toStr(String p) {
        String val = env.getProperty(p);
        return StringUtils.isEmpty(val) ? p : val;
    }

    public Integer toInteger(String p) {
        Integer val = env.getProperty(p, Integer.class);
        return Objects.isNull(val) ? -1 : val;
    }

    public void setMapError(Integer errorCode, List<String> args) {
        String messageError = "";

        for (String message : args) {
            messageError = messageError + " " + messageSource.getMessage(message, null, message, LocaleContextHolder.getLocale());
        }

        mapError.put(errorCode, messageError);
    }
}
