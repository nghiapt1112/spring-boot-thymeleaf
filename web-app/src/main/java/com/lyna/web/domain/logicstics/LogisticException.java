package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.exception.DomainException;

public class LogisticException extends DomainException {

    public LogisticException(int errorCode, String message) {
        super(Domain.Logistic.code(), errorCode, message);
    }
}
