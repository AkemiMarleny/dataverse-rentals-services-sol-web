package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Toponimo;

public record ToponimoPaginaDto(
        Long id,
        String nombre) {

    public static ToponimoPaginaDto fromEntity(Toponimo toponimo) {
        return new ToponimoPaginaDto(
                toponimo.getId(),
                toponimo.getNombre());
    }

}
