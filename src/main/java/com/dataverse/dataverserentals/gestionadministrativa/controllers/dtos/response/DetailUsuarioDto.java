package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

public record DetailUsuarioDto(
        Long id,
        Long personaId,
        String personaDenominacion,
        String nombreUsuario,
        RolUsuario rolCode,
        EstadoUsuario estadoCode,
        LocalDateTime fechaUltimoLogin,
        Boolean mfaEnabled,
        LocalDateTime fechaCreacion) {

    public static DetailUsuarioDto fromEntity(Usuario usuario) {
        return new DetailUsuarioDto(
                usuario.getId(),
                usuario.getPersonaId(),
                usuario.getPersona().getDenominacion(),
                usuario.getNombreUsuario(),
                usuario.getRolCode(),
                usuario.getEstadoCode(),
                usuario.getFechaUltimoLogin(),
                usuario.getMfaEnabled(),
                usuario.getFechaCreacion());
    }
}
