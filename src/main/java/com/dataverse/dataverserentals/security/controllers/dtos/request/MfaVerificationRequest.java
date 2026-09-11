package com.dataverse.dataverserentals.security.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MfaVerificationRequest(
        @NotBlank @NotNull String code) {
}
