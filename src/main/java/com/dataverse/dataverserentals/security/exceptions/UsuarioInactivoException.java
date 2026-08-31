package com.dataverse.dataverserentals.security.exceptions;

import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;

public class UsuarioInactivoException extends UnprocesableEntity {
    public UsuarioInactivoException(String message) {
        super(message);
    }
}