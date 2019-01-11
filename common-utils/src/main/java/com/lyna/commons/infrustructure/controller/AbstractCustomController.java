package com.lyna.commons.infrustructure.controller;

import com.lyna.commons.config.BaseValidator;
import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.commons.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class AbstractCustomController {

    protected static final Logger CONTROLLER_LOGGER = LoggerFactory.getLogger(AbstractCustomController.class);

    @Autowired
    protected Environment env;

    @Autowired
    private BaseValidator baseValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof AbstractObject) {
            boolean included = false;
            for (Validator validator : binder.getValidators()) {
                if (validator instanceof BaseValidator) {
                    included = true;
                    break;
                }
            }
            if (!included) {
                binder.addValidators(baseValidator);
            }
        }
    }


    @ExceptionHandler
    public ResponseEntity<String> handle(DomainException ex) {
        CONTROLLER_LOGGER.error("\n\tException: \n\t\texCode: {}, \n\t\texMessage: {}", ex.getCode(), ex.getErrorResponse().getError_description());
        return ResponseEntity
                .status(500)
                .body(JsonUtils.toJson(ex.getErrorResponse()));
    }
}
