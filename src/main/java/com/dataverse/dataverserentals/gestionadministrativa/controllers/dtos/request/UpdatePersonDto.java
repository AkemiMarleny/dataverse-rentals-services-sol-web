package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import java.time.LocalDate;

import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoCivil;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdatePersonDto(

        @NotNull TipoPersona tipoPersonaCode,

        // Campos comunes
        @NotNull TipoDocumento tipoDocumentoCode,
        @NotBlank @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El número de documento no sigue el formato requerido") String numeroDocumento,
        @NotBlank @Pattern(regexp = "^\\(\\+51\\) [0-9]{9}$", message = "Formato de teléfono incorrecto") String numeroCelular,
        @NotBlank @Email String correo,

        // Campos para persona natural
        String nombres,
        String apellidos,
        Genero generoCode,
        EstadoCivil estadoCivilCode,
        LocalDate fechaNacimiento,

        // Campo para persona natural con negocio
        String nombreComercial,

        // Campos para persona jurídica
        String razonSocial) {

}