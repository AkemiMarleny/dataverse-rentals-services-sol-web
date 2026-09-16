package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

public record UsuarioPaginaDto(
        Long id,
        Long personaId,
        String personaDenominacion,
        String nombreUsuario,
        RolUsuario rolCode,
        String rolNombre,
        EstadoUsuario estadoCode,
        String estadoNombre,
        LocalDateTime fechaUltimoLogin,
        Boolean mfaEnabled,
        Boolean mfaSetupRequired,
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
