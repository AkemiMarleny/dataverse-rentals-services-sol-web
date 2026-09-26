package com.dataverse.dataverserentals.security.controllers.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MfaSetupRequest(
        @NotBlank @NotNull
        @Schema(description = "Clave secreta TOTP generada por el endpoint de setup", example = "JBSWY3DPEHPK3PXP", requiredMode = Schema.RequiredMode.REQUIRED)
        String secret,
        @NotBlank @NotNull
        @Schema(description = "Código TOTP de 6 dígitos de la app autenticadora", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
        String code) {
}