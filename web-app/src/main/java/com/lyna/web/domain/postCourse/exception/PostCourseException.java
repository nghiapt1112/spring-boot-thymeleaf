package com.lyna.web.domain.postCourse.exception;

import com.lyna.commons.infrustructure.exception.DomainException;

public class PostCourseException extends DomainException {
    public PostCourseException(int errorCode, String message) {
        super(Domain.Package.code(), errorCode, message);
    }
}
