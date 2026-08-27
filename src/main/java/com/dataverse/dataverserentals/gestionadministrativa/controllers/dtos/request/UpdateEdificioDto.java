package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateEdificioDto(
        @NotBlank @Length(min = 3) String nombre,
        @NotBlank @Length(min = 3) String direccion,
        @NotNull Integer numeroPisos) {

}
