package com.lyna.web.domain.logicstics.exception;

import com.lyna.commons.infrustructure.exception.DomainException;

public class LogicsticException extends DomainException {
    public LogicsticException(int errorCode, String message) {
        super(Domain.User.code(), errorCode, message);
    }
}
