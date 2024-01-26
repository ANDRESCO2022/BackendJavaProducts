package com.mvn.springboot.restapi.app.crudjpa.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsExistDbValidation.class)
public @interface IsExistDb {
    String message() default "ya  existe en la BD";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
