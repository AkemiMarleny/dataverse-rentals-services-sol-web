package com.dataverse.dataverserentals.security.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

public record CurrentUserResponse(
        Long id,
        Long personaId,
        String nombreUsuario,
        RolUsuario rolCode,
        Boolean mfaEnabled,
        Boolean mfaSetupRequired,
        Boolean mfaConfigured,
        Boolean mfaVerified) {
}