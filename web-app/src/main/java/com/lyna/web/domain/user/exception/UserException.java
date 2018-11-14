package com.lyna.web.domain.user.exception;

import com.lyna.commons.infrustructure.exception.DomainException;

public class UserException extends DomainException {

    public UserException(int errorCode, String message) {
        super(101, errorCode, message);
    }
}
