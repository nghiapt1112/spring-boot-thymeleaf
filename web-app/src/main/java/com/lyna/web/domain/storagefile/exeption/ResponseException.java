package com.lyna.web.domain.storagefile.exeption;

public class ResponseException {

    private String message;
    private int errorCode;

    public ResponseException(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
