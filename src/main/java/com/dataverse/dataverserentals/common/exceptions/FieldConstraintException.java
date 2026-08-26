package com.dataverse.dataverserentals.common.exceptions;

import lombok.Getter;

@Getter
public class FieldConstraintException extends Exception {
    private final String field;
    private final String message;

    public FieldConstraintException(String field, String message) {
        super(String.format("%s: %s", field, message));
        this.field = field;
        this.message = message;
    }

}
