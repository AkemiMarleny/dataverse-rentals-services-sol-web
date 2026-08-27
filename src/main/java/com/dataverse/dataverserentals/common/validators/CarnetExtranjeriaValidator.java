package com.dataverse.dataverserentals.common.validators;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class CarnetExtranjeriaValidator extends BaseDocumentValidator {

    @Override
    protected Pattern getPattern() {
        return Pattern.compile("^\\d{9}$");
    }

    @Override
    protected String getErrorMessage() {
        return "Carné de Extranjería debe contener exactamente 9 dígitos numéricos.";
    }
}