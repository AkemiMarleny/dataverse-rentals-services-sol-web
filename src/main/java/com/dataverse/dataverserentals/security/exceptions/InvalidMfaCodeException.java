package com.dataverse.dataverserentals.security.exceptions;

import com.dataverse.dataverserentals.common.exceptions.Unauthorized;

public class InvalidMfaCodeException extends Unauthorized {

    public InvalidMfaCodeException(String message) {
        super(message);
    }
}
