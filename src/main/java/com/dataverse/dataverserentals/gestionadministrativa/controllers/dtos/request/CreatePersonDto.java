package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import java.time.LocalDate;

import com.dataverse.dataverserentals.common.annotations.ValidDocumentFormat;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreatePersonDto(
        @NotNull(message = "El tipo de persona es obligatorio") TipoPersona tipoPersonaCode,

        // Campos comunes
        @NotNull TipoDocumento tipoDocumentoCode,
        @NotBlank(message = "El número de documento es obligatorio") @ValidDocumentFormat String numeroDocumento,
        @NotBlank @Pattern(regexp = "^\\(\\+51\\) [0-9]{9}$", message = "Formato de teléfono incorrecto") String numeroCelular,
        @NotBlank @Email String correo,

        // Campos para persona natural
        String nombres,
        String apellidos,
        Genero generoCode,
        LocalDate fechaNacimiento,

        // Campo para persona natural con negocio
        String nombreComercial,

        // Campos para persona jurídica
        String razonSocial) {

}