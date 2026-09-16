package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionoperativa.entities.CategoriaGasto;

public record DetailCategoriaGastoDto(
        Long id,
        String nombre,
        String descripcion) {

    public static DetailCategoriaGastoDto fromEntity(CategoriaGasto categoriaGasto) {
        return new DetailCategoriaGastoDto(
                categoriaGasto.getId(),
                categoriaGasto.getNombre(),
                categoriaGasto.getDescripcion());
    }
}