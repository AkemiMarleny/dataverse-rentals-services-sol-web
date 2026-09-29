package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionoperativa.entities.CategoriaGasto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoriaGastoPaginaDto(
        @Schema(description = "ID de la categoría de gasto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "Nombre de la categoría de gasto", example = "Mantenimiento")
        String nombre,
        @Schema(description = "Descripción de la categoría de gasto", example = "Gastos de mantenimiento de áreas comunes")
        String descripcion) {

    public static CategoriaGastoPaginaDto fromEntity(CategoriaGasto categoriaGasto) {
        return new CategoriaGastoPaginaDto(
                categoriaGasto.getId(),
                categoriaGasto.getNombre(),
                categoriaGasto.getDescripcion());
    }
}