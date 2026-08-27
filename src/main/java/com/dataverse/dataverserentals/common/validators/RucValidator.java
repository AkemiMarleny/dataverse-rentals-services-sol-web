package com.dataverse.dataverserentals.common.validators;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;

@Component
public class RucValidator extends BaseDocumentValidator {

    @Override
    protected Pattern getPattern() {
        return Pattern.compile("^\\d{11}$");
    }

    @Override
    protected String getErrorMessage() {
        return "RUC debe contener exactamente 11 dígitos numéricos.";
    }

    @Override
    public void validate(String numeroDocumento) throws FieldConstraintException {
        super.validate(numeroDocumento);
        if (!numeroDocumento.startsWith("10") && !numeroDocumento.startsWith("20")) {
            throw new FieldConstraintException("numeroDocumento",
                    "RUC debe comenzar con '10' o '20'.");
        }
    }
}