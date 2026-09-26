package com.dataverse.dataverserentals.security.controllers.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MfaSetupResponse(
        @Schema(description = "Clave secreta TOTP para registrar en la app autenticadora", example = "JBSWY3DPEHPK3PXP", requiredMode = Schema.RequiredMode.REQUIRED)
        String secret,
        @Schema(description = "URL del código QR (otpauth://) para escanear", example = "otpauth://totp/Dataverse%20Rentals:admin?secret=JBSWY3DPEHPK3PXP&issuer=Dataverse%20Rentals")
        String qrCodeUrl,
        @Schema(description = "Códigos de respaldo de un solo uso", example = "[\"AB12CD34\", \"EF56GH78\"]")
        String[] backupCodes) {
}