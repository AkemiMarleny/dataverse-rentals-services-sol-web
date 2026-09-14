package com.dataverse.dataverserentals.common.exceptions;

import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

public class AccessDeniedException extends Exception {
    public AccessDeniedException(UsuarioPrincipal usuarioPrincipal) {
        super(String.format("usuario no autorizado con usuarioNombre: %s, usuarioRol: %s",
                usuarioPrincipal.usuarioNombre(),
                usuarioPrincipal.usuarioRol()));
    }
}
