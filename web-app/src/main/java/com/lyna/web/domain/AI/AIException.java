package com.lyna.web.domain.AI;

import com.lyna.commons.infrustructure.exception.DomainException;

public class AIException extends DomainException {
    private final static int AI_DOMAIN_CODE = 14;

    protected AIException(int errorCode, String message) {
        super(AI_DOMAIN_CODE, errorCode, message);
    }

    protected AIException(int errorCode, String message, Throwable e) {
        super(AI_DOMAIN_CODE, errorCode, message, e);
    }
}
