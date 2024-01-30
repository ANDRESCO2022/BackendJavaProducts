package com.mvn.springboot.restapi.app.crudjpa.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistByUserNameValidator.class)

public @interface ExistByUserName {
    String message() default "Usuario ya existe en la base  de datos,ingrese otro ususario!!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
