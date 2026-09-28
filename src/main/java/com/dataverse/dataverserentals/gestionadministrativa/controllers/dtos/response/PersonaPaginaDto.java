package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.time.LocalDate;
import java.util.Optional;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

import io.swagger.v3.oas.annotations.media.Schema;

public record PersonaPaginaDto(
        @Schema(description = "ID de la persona", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "Tipo de persona", example = "NATURAL_SIN_NEGOCIO")
        TipoPersona tipoPersonaCode,
        @Schema(description = "Nombre legible del tipo de persona", example = "Natural sin negocio")
        String tipoPersonaNombre,
        @Schema(description = "Denominación de la persona", example = "Juan Carlos Pérez Gómez")
        String denominacion,
        @Schema(description = "Tipo de documento", example = "DNI")
        TipoDocumento tipoDocumentoCode,
        @Schema(description = "Nombre legible del tipo de documento", example = "Dni")
        String tipoDocumentoNombre,
        @Schema(description = "Número de documento", example = "12345678")
        String numeroDocumento,
        @Schema(description = "Número de celular", example = "(+51) 999888777")
        String numeroCelular,
        @Schema(description = "Correo electrónico", example = "juan.perez@example.com")
        String correo,

        // Campos para persona natural
        @Schema(description = "Nombres", example = "Juan Carlos")
        String nombres,
        @Schema(description = "Apellidos", example = "Pérez Gómez")
        String apellidos,
        @Schema(description = "Género", example = "MASCULINO")
        Genero generoCode,
        @Schema(description = "Nombre legible del género", example = "Masculino")
        String generoNombre,
        @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
        LocalDate fechaNacimiento,

        // Campo para persona natural con negocio
        @Schema(description = "Nombre comercial", example = "Bodega Pérez")
        String nombreComercial,

        // Campos para persona jurídica
        @Schema(description = "Razón social", example = "Pérez & Hijos S.A.C.")
        String razonSocial) {

    public static PersonaPaginaDto fromEntity(Persona persona) {
        Optional<Genero> personaGenero = Optional.ofNullable(persona.getGeneroCode());

        return new PersonaPaginaDto(
                persona.getId(),
                persona.getTipoPersonaCode(),
                persona.getTipoPersonaCode().intoString(),
                persona.getDenominacion(),
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