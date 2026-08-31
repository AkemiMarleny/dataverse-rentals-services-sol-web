package com.dataverse.dataverserentals.security.exceptions;

import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;

public class UsuarioBloqueadoException extends UnprocesableEntity {
    public UsuarioBloqueadoException(String message) {
        super(message);
    }
}