package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUsuarioDto(
        @NotNull() Long personaId,

        @NotBlank() String nombreUsuario,

        @NotBlank() String contrasena,

        @NotBlank() String confirmarContrasena,

        @NotNull() RolUsuario rolCode,

        @NotNull() EstadoUsuario estadoCode) {
}