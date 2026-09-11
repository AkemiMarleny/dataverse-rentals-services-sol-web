package com.dataverse.dataverserentals.common.exceptions;

import lombok.Getter;

@Getter
public class Unauthorized extends Exception {
    private final String message;

    public Unauthorized(String message) {
        super(message);
        this.message = message;
    }
}
