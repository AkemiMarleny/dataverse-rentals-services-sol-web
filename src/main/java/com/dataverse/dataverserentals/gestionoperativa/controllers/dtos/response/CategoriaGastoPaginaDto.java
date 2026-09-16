package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionoperativa.entities.CategoriaGasto;

public record CategoriaGastoPaginaDto(
        Long id,
        String nombre,
        String descripcion) {

    public static CategoriaGastoPaginaDto fromEntity(CategoriaGasto categoriaGasto) {
        return new CategoriaGastoPaginaDto(
                categoriaGasto.getId(),
                categoriaGasto.getNombre(),
                categoriaGasto.getDescripcion());
    }
}