package com.dataverse.dataverserentals.security.controllers.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank()
        @Schema(description = "Nombre de usuario", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombreUsuario,

        @NotBlank()
        @Schema(description = "Contraseña del usuario", example = "admin123", format = "password", requiredMode = Schema.RequiredMode.REQUIRED)
        String contrasena) {
}