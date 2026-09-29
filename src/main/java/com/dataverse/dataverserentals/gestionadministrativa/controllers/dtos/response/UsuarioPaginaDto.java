package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

import io.swagger.v3.oas.annotations.media.Schema;

public record UsuarioPaginaDto(
        @Schema(description = "ID del usuario", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID de la persona asociada", example = "1")
        Long personaId,
        @Schema(description = "Denominación de la persona", example = "Juan Carlos Pérez Gómez")
        String personaDenominacion,
        @Schema(description = "Nombre de usuario", example = "jperez")
        String nombreUsuario,
        @Schema(description = "Rol del usuario", example = "OPERARIO")
        RolUsuario rolCode,
        @Schema(description = "Nombre legible del rol", example = "Operario")
        String rolNombre,
        @Schema(description = "Estado del usuario", example = "ACTIVO")
        EstadoUsuario estadoCode,
        @Schema(description = "Nombre legible del estado", example = "Activo")
        String estadoNombre,
        @Schema(description = "Fecha del último inicio de sesión")
        LocalDateTime fechaUltimoLogin,
        @Schema(description = "Indica si el MFA está habilitado", example = "false")
        Boolean mfaEnabled,
        @Schema(description = "Indica si el usuario debe configurar el MFA", example = "false")
        Boolean mfaSetupRequired,
        @Schema(description = "Fecha de creación del usuario")
        LocalDateTime fechaCreacion) {

    public static UsuarioPaginaDto fromEntity(Usuario usuario) {
        return new UsuarioPaginaDto(
                usuario.getId(),
                usuario.getPersonaId(),
                usuario.getPersona().getDenominacion(),
                usuario.getNombreUsuario(),
                usuario.getRolCode(),
                usuario.getRolCode().intoString(),
                usuario.getEstadoCode(),
                usuario.getEstadoCode().intoString(),
                usuario.getFechaUltimoLogin(),
                usuario.getMfaEnabled(),
                usuario.getMfaSetupRequired(),
                usuario.getFechaCreacion());
    }
}