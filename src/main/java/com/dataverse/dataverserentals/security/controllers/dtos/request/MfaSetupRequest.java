package com.dataverse.dataverserentals.security.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MfaSetupRequest(
        @NotBlank @NotNull String secret,
        @NotBlank @NotNull String code) {
}
