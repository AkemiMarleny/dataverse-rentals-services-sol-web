package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEdificioDto(
        @NotBlank @Length(min = 3) String nombre,
        @NotBlank @Length(min = 3) String direccion,
        @NotNull @Min(1) @Max(10) Integer numeroPisos) {

}
