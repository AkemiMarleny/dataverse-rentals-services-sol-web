package com.dataverse.dataverserentals.common.validators;

import java.util.regex.Pattern;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;

public abstract class BaseDocumentValidator implements DocumentValidator {

    protected abstract Pattern getPattern();

    protected abstract String getErrorMessage();

    @Override
    public void validate(String numeroDocumento) throws FieldConstraintException {
        if (!getPattern().matcher(numeroDocumento).matches()) {
            throw new FieldConstraintException("numeroDocumento", getErrorMessage());
        }
    }
}