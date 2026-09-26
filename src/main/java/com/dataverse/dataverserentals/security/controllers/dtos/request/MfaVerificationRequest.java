package com.dataverse.dataverserentals.security.controllers.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MfaVerificationRequest(
        @NotBlank @NotNull
        @Schema(description = "Código TOTP de 6 dígitos o código de respaldo", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
        String code) {
}