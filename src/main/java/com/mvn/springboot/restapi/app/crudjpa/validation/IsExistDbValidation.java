package com.mvn.springboot.restapi.app.crudjpa.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mvn.springboot.restapi.app.crudjpa.Services.ProductService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class IsExistDbValidation implements ConstraintValidator<IsExistDb, String> {
    @Autowired
    private ProductService productService;

    @Override
    public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
        if (productService == null) {
            return true;

        }

        return !productService.existSku(arg0);
    }

}
