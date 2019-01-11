package com.lyna.commons.infrustructure.exception;

import com.lyna.commons.infrustructure.object.AbstractObject;

public class DomainException extends RuntimeException {
    public static final int GENERAL_ERROR = 1;
    private static final long serialVersionUID = 5262974602102886958L;
    public static final int UNIT_SIZE = 1000;

    private int domainCode;

    private int errorCode = GENERAL_ERROR;

    private DomainException() {
    }

    public DomainException(int errorCode, String message) {
        super(message);
        this.domainCode = Domain.General.code();
        this.errorCode = errorCode;
    }

    public DomainException(int errorCode, Throwable e) {
        super(e);
        this.domainCode = Domain.General.code();
        this.errorCode = errorCode;
    }

    public DomainException(String message) {
        super(message);
        this.domainCode = Domain.General.code();
    }

    protected DomainException(int domainCode, int errorCode) {
        this.domainCode = domainCode;
        this.errorCode = errorCode;
    }

    protected DomainException(int domainCode, int errorCode, Throwable e) {
        super(e);
        this.domainCode = domainCode;
        this.errorCode = errorCode;
    }

    protected DomainException(int domainCode, int errorCode, String message) {
        super(message);
        this.domainCode = domainCode;
        this.errorCode = errorCode;
    }

    protected DomainException(int domainCode, int errorCode, String message, Throwable e) {
        super(message, e);
        this.domainCode = domainCode;
        this.errorCode = errorCode;
    }

    public int getCode() {
        return calculateCode(domainCode, errorCode);
    }

    public static int calculateCode(int domainCode, int errorCode) {
        return domainCode * UNIT_SIZE + errorCode;
    }

    public Error getErrorResponse() {
        return new Error(this.getCode(), super.getMessage());
    }

    public enum Domain {
        General(1), Validate(2), User(3), CSV(4), Store(5), Product(6), Package(7),
        PostCourse(11), Resources(13);

        private int domainCode;

        Domain(int domainCode) {
            this.domainCode = domainCode;
        }

        public int code() {
            return domainCode;
        }

    }

    public static class Error extends AbstractObject {
        private int error;
        private String error_description;

        public Error() {
        }

        public Error(int error, String error_description) {
            this.error = error;
            this.error_description = error_description;
        }

        public int getError() {
            return error;
        }

        public void setError(int error) {
            this.error = error;
        }

        public String getError_description() {
            return error_description;
        }

        public void setError_description(String error_description) {
            this.error_description = error_description;
        }

    }
}
