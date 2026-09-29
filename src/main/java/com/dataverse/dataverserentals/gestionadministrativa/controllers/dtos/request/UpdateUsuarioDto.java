package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUsuarioDto(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Schema(description = "Nombre de usuario", example = "jperez", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombreUsuario,
        @NotNull(message = "El rol es obligatorio")
        @Schema(description = "Rol del usuario", example = "OPERARIO", requiredMode = Schema.RequiredMode.REQUIRED)
        RolUsuario rolCode,
        @NotNull(message = "El estado es obligatorio")
        @Schema(description = "Estado del usuario", example = "ACTIVO", requiredMode = Schema.RequiredMode.REQUIRED)
        EstadoUsuario estadoCode,
        @NotNull(message = "El estado de MFA es obligatorio")
        @Schema(description = "Indica si el MFA está habilitado", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
        Boolean mfaEnabled) {
}