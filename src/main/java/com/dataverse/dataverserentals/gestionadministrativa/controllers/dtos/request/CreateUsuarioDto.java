package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUsuarioDto(
        @NotNull()
        @Schema(description = "ID de la persona asociada al usuario", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long personaId,

        @NotBlank()
        @Schema(description = "Nombre de usuario", example = "jperez", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombreUsuario,

        @NotBlank()
        @Schema(description = "Contraseña del usuario", example = "claveSegura123", format = "password", requiredMode = Schema.RequiredMode.REQUIRED)
        String contrasena,

        @NotBlank()
        @Schema(description = "Confirmación de la contraseña (debe coincidir)", example = "claveSegura123", format = "password", requiredMode = Schema.RequiredMode.REQUIRED)
        String confirmarContrasena,

        @NotNull()
        @Schema(description = "Rol del usuario (ADMINISTRADOR, ANALISTA_FINANZAS, OPERARIO, INQUILINO, SYSTEM)", example = "OPERARIO", requiredMode = Schema.RequiredMode.REQUIRED)
        RolUsuario rolCode,

        @NotNull()
        @Schema(description = "Estado del usuario (ACTIVO, INACTIVO, BLOQUEADO)", example = "ACTIVO", requiredMode = Schema.RequiredMode.REQUIRED)
        EstadoUsuario estadoCode) {
}