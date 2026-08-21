package com.dataverse.dataverserentals.administrations.controllers.dtos.response;

import java.sql.Timestamp;

import com.dataverse.dataverserentals.administrations.entities.Persona;
import com.dataverse.dataverserentals.administrations.enums.EstadoCivil;
import com.dataverse.dataverserentals.administrations.enums.Genero;
import com.dataverse.dataverserentals.administrations.enums.TipoDocumento;

public record PersonaPaginaDto(
        Long id,
        String nombres,
        String apellidos,
        TipoDocumento tipoDocumentoCode,
        String numeroDocumento,
        Genero generoCode,
        EstadoCivil estadoCivilCode,
        Timestamp fechaNacimiento,
        String numeroCelular,
        String correo) {

    public static PersonaPaginaDto fromEntity(Persona persona) {
        return new PersonaPaginaDto(
                persona.getId(),
                persona.getNombres(),
                persona.getApellidos(),
                persona.getTipoDocumentoCode(),
                persona.getNumeroDocumento(),
                persona.getGeneroCode(),
                persona.getEstadoCivilCode(),
                Timestamp.valueOf(persona.getFechaNacimiento().atStartOfDay()),
                persona.getNumeroCelular(),
                persona.getCorreo());
    }

}
