package com.dataverse.dataverserentals.security.controllers.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(
        @NotBlank()
        @Schema(description = "Contraseña actual", example = "admin123", format = "password", requiredMode = Schema.RequiredMode.REQUIRED)
        String contrasenaAnterior,
        @NotBlank()
        @Schema(description = "Nueva contraseña", example = "nuevaClave123", format = "password", requiredMode = Schema.RequiredMode.REQUIRED)
        String contrasenaNueva,
        @NotBlank()
        @Schema(description = "Confirmación de la nueva contraseña", example = "nuevaClave123", format = "password", requiredMode = Schema.RequiredMode.REQUIRED)
        String confirmarContrasena) {
}