package com.dataverse.dataverserentals.common.exceptions;

import lombok.Getter;

@Getter
public class UnprocesableEntity extends Exception {
    private final String message;

    public UnprocesableEntity(String message) {
        super(message);
        this.message = message;
    }

}
