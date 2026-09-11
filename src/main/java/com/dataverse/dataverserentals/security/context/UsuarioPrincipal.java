package com.dataverse.dataverserentals.security.context;

import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.security.enums.TokenScope;

public record UsuarioPrincipal(
        Long usuarioId,
        String usuarioNombre,
        RolUsuario usuarioRol,
        Long personaId,
        TokenScope scope) {
}
