package com.dataverse.dataverserentals.security.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank() String nombreUsuario,

        @NotBlank() String contrasena) {
}
