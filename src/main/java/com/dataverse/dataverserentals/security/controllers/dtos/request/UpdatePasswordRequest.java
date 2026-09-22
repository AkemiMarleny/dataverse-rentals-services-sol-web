package com.dataverse.dataverserentals.security.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(
        @NotBlank() String contrasenaAnterior,
        @NotBlank() String contrasenaNueva,
        @NotBlank() String confirmarContrasena) {
}
