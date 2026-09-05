package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.time.LocalDate;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

public record DetailPersonaDto(
        Long id,
        TipoPersona tipoPersonaCode,
        TipoDocumento tipoDocumentoCode,
        String numeroDocumento,
        String numeroCelular,
        String correo,

        // Campos para persona natural
        String nombres,
        String apellidos,
        Genero generoCode,
        LocalDate fechaNacimiento,

        // Campo para persona natural con negocio
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
                persona.getFechaNacimiento(),
                persona.getNombreComercial(),
                persona.getRazonSocial());
    }

}