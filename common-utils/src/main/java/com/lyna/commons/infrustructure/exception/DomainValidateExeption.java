package com.lyna.commons.infrustructure.exception;

public class DomainValidateExeption extends DomainException {
    private static final int DEFAULT_ENTITY_ERROR_CODE = 1;

    public DomainValidateExeption(String message) {
        super(Domain.Validate.code(), DEFAULT_ENTITY_ERROR_CODE, message);
    }

}
