package com.lyna.web.domain.postCourse.Exception;

import com.lyna.commons.infrustructure.exception.DomainException;

public class PostCourseException extends DomainException {

    public PostCourseException(int errorCode, String message) {
        super(Domain.User.code(), errorCode, message);
    }
}
