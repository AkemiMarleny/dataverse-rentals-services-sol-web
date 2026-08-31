package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreatePersonaJuridicaDto(
        @NotBlank @Length(min = 3) String razonSocial,
        @NotNull TipoDocumento tipoDocumentoCode,
        @NotBlank @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El número de documento no sigue el formato requerido") String numeroDocumento,
        @NotBlank @Email String correo,
        @NotBlank @Pattern(regexp = "^\\(\\+51\\) [0-9]{9}$", message = "Formato de teléfono incorrecto") String telefono) {

}