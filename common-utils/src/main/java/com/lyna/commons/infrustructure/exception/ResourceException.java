package com.lyna.commons.infrustructure.exception;

public class ResourceException extends DomainException {

    public ResourceException(int errorCode, String message, Throwable e) {
        super(Domain.Resources.code(), errorCode, message, e);
    }

}