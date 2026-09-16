package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateEdificioDto(
        @NotBlank(message = "Debe ingresar el nombre del edificio") @Length(min = 3) String nombre,
        @NotBlank(message = "Debe ingresar la dirección del edificio") @Length(min = 3) String direccion,
        @NotNull(message = "Debe ingresar el número de pisos del edificio") @Min(value = 1, message = "El número mínimo de pisos permitido es 1") @Max(value = 10, message = "El número máximo de pisos permitido es 10") Integer numeroPisos) {
}
