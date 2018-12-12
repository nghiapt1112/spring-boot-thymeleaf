package com.lyna.web.domain.delivery.exception;

import com.lyna.commons.infrustructure.exception.DomainException;

public class DeliveryException extends DomainException {
    public DeliveryException(int errorCode, String message) {
        super(Domain.Delivery.code(), errorCode, message);
    }
}
