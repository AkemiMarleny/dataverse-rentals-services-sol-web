package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoriaGastoDto(
        @NotBlank(message = "Debe indicar el nombre de la categoría")
        @Schema(description = "Nombre de la categoría de gasto", example = "Mantenimiento", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombre,
        @NotBlank(message = "Debe indicar o especificar la categoria de gasto")
        @Schema(description = "Descripción de la categoría de gasto", example = "Gastos de mantenimiento de áreas comunes", requiredMode = Schema.RequiredMode.REQUIRED)
        String descripcion) {
}