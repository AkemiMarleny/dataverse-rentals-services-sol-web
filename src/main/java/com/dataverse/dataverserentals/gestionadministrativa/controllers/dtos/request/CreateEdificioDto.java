package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEdificioDto(
        @NotBlank(message = "Debe ingresar el nombre del edificio") @Length(min = 3)
        @Schema(description = "Nombre del edificio (mínimo 3 caracteres)", example = "Torre Central", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombre,
        @NotNull(message = "Debe ingresar el número de pisos del edificio") @Min(value = 1, message = "El número mínimo de pisos permitido es 1") @Max(value = 10, message = "El número máximo de pisos permitido es 10")
        @Schema(description = "Número de pisos (1-10)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer numeroPisos) {

}