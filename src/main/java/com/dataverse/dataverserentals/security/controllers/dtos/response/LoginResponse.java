package com.dataverse.dataverserentals.security.controllers.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "Access token JWT", example = "eyJhbGciOiJIUzI1NiJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
        String token,
        @Schema(description = "Tipo de token", example = "Bearer")
        String tipo,
        @Schema(description = "Estado de la autenticación o del flujo MFA", example = "MFA_REQUIRED")
        String status) {
}