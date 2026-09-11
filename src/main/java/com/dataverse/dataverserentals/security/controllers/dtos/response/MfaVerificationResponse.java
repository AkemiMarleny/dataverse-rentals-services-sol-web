package com.dataverse.dataverserentals.security.controllers.dtos.response;

public record MfaVerificationResponse(
        String token,
        String tipo,
        boolean mfaRequired,
        boolean setupRequired) {
}
