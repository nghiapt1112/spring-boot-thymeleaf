package com.lyna.web.domain.stores.exception;

import com.lyna.commons.infrustructure.exception.DomainException;

public class StoreException extends DomainException {

    public StoreException(int errorCode, String message) {
        super(Domain.User.code(), errorCode, message);
    }
}
