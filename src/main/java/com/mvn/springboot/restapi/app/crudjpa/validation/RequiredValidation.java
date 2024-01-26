package com.mvn.springboot.restapi.app.crudjpa.validation;

import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequiredValidation implements ConstraintValidator<IsRequired, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext contex) {
        //return (value != null && !value.isEmpty() && value.isBlank());
        return StringUtils.hasText(value);
    }

}
