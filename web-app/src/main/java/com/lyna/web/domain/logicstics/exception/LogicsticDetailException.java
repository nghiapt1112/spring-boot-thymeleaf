package com.lyna.web.domain.logicstics.exception;


import com.lyna.commons.infrustructure.exception.DomainException;

public class LogicsticDetailException extends DomainException {
    public LogicsticDetailException(int errorCode, String message) {
        super(Domain.User.code(), errorCode, message);
    }
}
