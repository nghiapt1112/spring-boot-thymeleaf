package com.lyna.commons.config;

import com.lyna.commons.infrustructure.exception.DomainValidateExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class BaseValidator implements org.springframework.validation.Validator {

    protected final Logger LOGGER = LoggerFactory.getLogger(BaseValidator.class);
    protected ApplicationContext applicationContext;

    @Autowired
    protected Validator validator;

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
        if (constraintViolations.size() > 0) {
            throw new DomainValidateExeption("Requested object is invalid.");
        }
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.rejectValue(propertyPath, "", message);
        }
        addExtraValidation(target, errors);

    }

    public void addExtraValidation(Object target, Errors errors) {
        // TODO: for override validation.
    }
}
