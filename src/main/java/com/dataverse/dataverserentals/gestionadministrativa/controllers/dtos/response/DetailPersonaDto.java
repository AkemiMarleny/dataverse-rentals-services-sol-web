package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.sql.Timestamp;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoCivil;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

public record DetailPersonaDto(
        Long id,
        TipoPersona tipoPersona,
        TipoDocumento tipoDocumentoCode,
        String numeroDocumento,
        String numeroCelular,
        String correo,

        // Campos para persona natural
        String nombres,
        String apellidos,
        Genero generoCode,
        EstadoCivil estadoCivilCode,
        Timestamp fechaNacimiento,
        String nombreComercial,

        // Campos para persona jurídica
        String razonSocial) {

    public static DetailPersonaDto fromEntity(Persona persona) {
        return new DetailPersonaDto(
                persona.getId(),
                persona.getTipoPersonaCode(),
                persona.getTipoDocumentoCode(),
                persona.getNumeroDocumento(),
                persona.getNumeroCelular(),
                persona.getCorreo(),
                persona.getNombres(),
                persona.getApellidos(),
                persona.getGeneroCode(),
                persona.getEstadoCivilCode(),
                persona.getFechaNacimiento() != null ? Timestamp.valueOf(persona.getFechaNacimiento().atStartOfDay())
                        : null,
                persona.getNombreComercial(),
                persona.getRazonSocial());
    }

}