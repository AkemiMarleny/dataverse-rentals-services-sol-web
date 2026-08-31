package com.dataverse.dataverserentals.security.exceptions;

import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;

public class CredencialesInvalidasException extends UnprocesableEntity {
    public CredencialesInvalidasException(String message) {
        super(message);
    }
}