package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUsuarioDto(
        @NotBlank(message = "El nombre de usuario es obligatorio") String nombreUsuario,
        @NotNull(message = "El rol es obligatorio") RolUsuario rolCode,
        @NotNull(message = "El estado es obligatorio") EstadoUsuario estadoCode,
        @NotNull(message = "El estado de MFA es obligatorio") Boolean mfaEnabled) {
}
