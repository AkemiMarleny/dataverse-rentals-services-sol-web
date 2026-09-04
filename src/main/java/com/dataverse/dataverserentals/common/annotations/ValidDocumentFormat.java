package com.dataverse.dataverserentals.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DocumentFormatValidator.class)
@Documented
public @interface ValidDocumentFormat {

    String message() default "El número de documento no sigue el formato requerido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}