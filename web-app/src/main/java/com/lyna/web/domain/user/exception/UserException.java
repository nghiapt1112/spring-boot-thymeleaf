package com.lyna.web.domain.user.exception;

import com.lyna.commons.infrustructure.exception.DomainException;

public class UserException extends DomainException {

    public UserException(int errorCode, String message) {
        super(Domain.User.code(), errorCode, message);
    }
}
