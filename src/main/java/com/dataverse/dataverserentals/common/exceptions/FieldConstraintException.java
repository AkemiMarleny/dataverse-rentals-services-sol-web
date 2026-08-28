package com.dataverse.dataverserentals.common.exceptions;

import lombok.Getter;

@Getter
public class FieldConstraintException extends RuntimeException {

    private final String field;

    public FieldConstraintException(String field, String message) {
        super(message);
        this.field = field;
    }

}
