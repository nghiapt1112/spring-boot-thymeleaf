package com.lyna.web.domain.mpackage.exception;

import com.lyna.commons.infrustructure.exception.DomainException;

public class PackageException extends DomainException {
    public PackageException(int errorCode, String message) {
        super(Domain.User.code(), errorCode, message);
    }
}
