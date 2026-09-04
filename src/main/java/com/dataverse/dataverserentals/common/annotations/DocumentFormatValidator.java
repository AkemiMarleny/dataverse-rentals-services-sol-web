package com.dataverse.dataverserentals.common.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentFormatValidator implements ConstraintValidator<ValidDocumentFormat, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return value.matches("^[A-Za-z0-9]+$");
    }
}