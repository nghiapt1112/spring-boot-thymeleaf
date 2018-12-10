package com.lyna.web.domain.product.exeption;

import com.lyna.commons.infrustructure.exception.DomainException;

public class ProductException extends DomainException {
    public ProductException(int errorCode, String message) {
        super(Domain.Product.code(), errorCode, message);
    }
}
