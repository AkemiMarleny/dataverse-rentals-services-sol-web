package com.dataverse.dataverserentals.security.exceptions;

import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;

public class MfaAlreadyEnabledException extends UnprocesableEntity {
    public MfaAlreadyEnabledException(String message) {
        super(message);
    }
}
