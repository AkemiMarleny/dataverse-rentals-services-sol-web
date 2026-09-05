package com.dataverse.dataverserentals.security.context;

import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

public record UsuarioPrincipal(
        Long usuarioId,
        String usuarioNombre,
        RolUsuario usuarioRol,
        Long personaId) {
}
