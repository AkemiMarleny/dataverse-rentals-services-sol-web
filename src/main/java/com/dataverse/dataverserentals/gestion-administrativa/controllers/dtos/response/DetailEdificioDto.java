package com.dataverse.dataverserentals.administrations.controllers.dtos.response;

import com.dataverse.dataverserentals.administrations.entities.Edificio;

public record DetailEdificioDto(
        Long id,
        String nombre,
        String direccion,
        Integer numeroPisos) {

    public static DetailEdificioDto fromEntity(Edificio edificio) {
        return new DetailEdificioDto(
                edificio.getId(),
                edificio.getNombre(),
                edificio.getDireccion(),
                edificio.getNumeroPisos());
    }

}
