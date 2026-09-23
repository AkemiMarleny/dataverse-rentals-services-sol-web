package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Toponimo;

public record DetailToponimoDto(
        Long id,
        String nombre) {

    public static DetailToponimoDto fromEntity(Toponimo toponimo) {
        return new DetailToponimoDto(
                toponimo.getId(),
                toponimo.getNombre());
    }

}
