package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.time.LocalDate;
import java.util.Optional;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

public record DetailPersonaDto(
        Long id,
        TipoPersona tipoPersonaCode,
        String tipoPersonaNombre,
        TipoDocumento tipoDocumentoCode,
        String tipoDocumentoNombre,
        String numeroDocumento,
        String numeroCelular,
        String correo,

        // Campos para persona natural
        String nombres,
        String apellidos,
        Genero generoCode,
        String generoNombre,
        LocalDate fechaNacimiento,

        // Campo para persona natural con negocio
        String nombreComercial,

        // Campos para persona jurídica
        String razonSocial) {

    public static DetailPersonaDto fromEntity(Persona persona) {
        Optional<Genero> personaGenero = Optional.ofNullable(persona.getGeneroCode());

        return new DetailPersonaDto(
                persona.getId(),
                persona.getTipoPersonaCode(),
                persona.getTipoPersonaCode().intoString(),
                persona.getTipoDocumentoCode(),
                persona.getTipoDocumentoCode().intoString(),
                persona.getNumeroDocumento(),
                persona.getNumeroCelular(),
                persona.getCorreo(),
                persona.getNombres(),
                persona.getApellidos(),
                persona.getGeneroCode(),
                personaGenero.map(Genero::intoString).orElse(null),
                persona.getFechaNacimiento(),
                persona.getNombreComercial(),
                persona.getRazonSocial());
    }

}