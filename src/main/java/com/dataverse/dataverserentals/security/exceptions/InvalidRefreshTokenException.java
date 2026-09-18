package com.dataverse.dataverserentals.security.exceptions;

import com.dataverse.dataverserentals.common.exceptions.Unauthorized;

public class InvalidRefreshTokenException extends Unauthorized {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
