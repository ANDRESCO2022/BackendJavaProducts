package com.mvn.springboot.restapi.app.crudjpa;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mvn.springboot.restapi.app.crudjpa.entities.Product;

@Component
public class ProductValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", null,"es requerido!!");
        // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description",
        // "NotEmpty.product.description");
        if (product.getDescription() == null || product.getDescription().isBlank()) {
            errors.rejectValue("description", null,"hace falta una descripción!!");
        }
        if (product.getPrice() == null ) {
            errors.rejectValue("price",null, "no puede ser nulo!!!");
        }else if (product.getPrice() <= 500) {
            errors.rejectValue("price", null, "debe ser mayor a 500!!!");
        }

    }

}
