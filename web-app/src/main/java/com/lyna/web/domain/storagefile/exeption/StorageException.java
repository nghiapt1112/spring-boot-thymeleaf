package com.lyna.web.domain.storagefile.exeption;

import com.lyna.commons.infrustructure.exception.DomainException;

public class StorageException extends DomainException {
    public StorageException(String message){super(message);}
    public StorageException(String message, Throwable cause) {
        super(Domain.CSV.code(), cause);
    }
}
