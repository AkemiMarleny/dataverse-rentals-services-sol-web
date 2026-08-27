package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoCivil;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreatePersonDto(
        @NotBlank @Length(min = 3) String nombres,
        @NotBlank @Length(min = 3) String apellidos,
        @NotNull TipoDocumento tipoDocumentoCode,
        @NotBlank @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El número de documento no sigue el formato requerido") String numeroDocumento,
        @NotNull Genero generoCode,
        @NotNull EstadoCivil estadoCivilCode,
        @NotNull LocalDate fechaNacimiento,
        @NotBlank @Pattern(regexp = "^\\(\\+51\\) [0-9]{9}$", message = "Formato de teléfono incorrecto") String numeroCelular,
        @NotBlank @Email String correo) {

}
