package com.dataverse.dataverserentals.common.validators;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;

public interface DocumentValidator {
    void validate(String numeroDocumento) throws FieldConstraintException;
}