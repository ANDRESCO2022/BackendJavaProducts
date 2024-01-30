package com.mvn.springboot.restapi.app.crudjpa.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mvn.springboot.restapi.app.crudjpa.Services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component

public class ExistByUserNameValidator implements ConstraintValidator<ExistByUserName, String> {
    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext arg1) {
        if (userService == null) {
            return true;
            
        }
        return !userService.existByUserName(username);
    }

}
