package com.dataverse.dataverserentals.security.controllers.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MfaVerificationResponse(
        @Schema(description = "Access token JWT cuando el código es válido", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,
        @Schema(description = "Tipo de token", example = "Bearer")
        String tipo,
        @Schema(description = "Indica si se requiere la verificación MFA", example = "false")
        boolean mfaRequired,
        @Schema(description = "Indica si se requiere configurar el MFA", example = "false")
        boolean setupRequired) {
}