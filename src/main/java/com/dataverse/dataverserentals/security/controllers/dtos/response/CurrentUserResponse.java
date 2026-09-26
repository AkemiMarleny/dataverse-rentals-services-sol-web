package com.dataverse.dataverserentals.security.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrentUserResponse(
        @Schema(description = "ID del usuario", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID de la persona asociada", example = "1")
        Long personaId,
        @Schema(description = "Nombre de usuario", example = "admin")
        String nombreUsuario,
        @Schema(description = "Rol del usuario", example = "ADMINISTRADOR")
        RolUsuario rolCode,
        @Schema(description = "Indica si el MFA está habilitado", example = "true")
        Boolean mfaEnabled,
        @Schema(description = "Indica si el usuario debe configurar el MFA", example = "false")
        Boolean mfaSetupRequired,
        @Schema(description = "Indica si el MFA está configurado", example = "true")
        Boolean mfaConfigured,
        @Schema(description = "Indica si el MFA ya fue verificado en la sesión", example = "true")
        Boolean mfaVerified) {
}