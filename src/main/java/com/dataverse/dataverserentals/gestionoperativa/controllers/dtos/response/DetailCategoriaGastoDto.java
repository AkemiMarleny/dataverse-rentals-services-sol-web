package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionoperativa.entities.CategoriaGasto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DetailCategoriaGastoDto(
        @Schema(description = "ID de la categoría de gasto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "Nombre de la categoría de gasto", example = "Mantenimiento")
        String nombre,
        @Schema(description = "Descripción de la categoría de gasto", example = "Gastos de mantenimiento de áreas comunes")
        String descripcion) {

    public static DetailCategoriaGastoDto fromEntity(CategoriaGasto categoriaGasto) {
        return new DetailCategoriaGastoDto(
                categoriaGasto.getId(),
                categoriaGasto.getNombre(),
                categoriaGasto.getDescripcion());
    }
}