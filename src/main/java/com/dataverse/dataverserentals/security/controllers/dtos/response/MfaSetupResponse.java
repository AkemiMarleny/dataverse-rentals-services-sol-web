package com.dataverse.dataverserentals.security.controllers.dtos.response;

public record MfaSetupResponse(
        String secret,
        String qrCodeUrl,
        String[] backupCodes) {
}
