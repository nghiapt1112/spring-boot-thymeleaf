package com.lyna.web.domain.delivery.exception;

import com.lyna.commons.infrustructure.exception.DomainException;

public class DeliveryDetailException extends DomainException {
    public DeliveryDetailException(int errorCode, String message) {
        super(Domain.User.code(), errorCode, message);
    }
}
