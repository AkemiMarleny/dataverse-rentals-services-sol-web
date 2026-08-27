package com.dataverse.dataverserentals.common.validators;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class DniValidator extends BaseDocumentValidator {

    @Override
    protected Pattern getPattern() {
        return Pattern.compile("^\\d{8}$");
    }

    @Override
    protected String getErrorMessage() {
        return "DNI debe contener exactamente 8 dígitos numéricos.";
    }
}