package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;

public record DetailEdificioDto(
        Long id,
        String nombre,
        Integer numeroPisos) {

    public static DetailEdificioDto fromEntity(Edificio edificio) {
        return new DetailEdificioDto(
                edificio.getId(),
                edificio.getNombre(),
                edificio.getNumeroPisos());
    }

}
