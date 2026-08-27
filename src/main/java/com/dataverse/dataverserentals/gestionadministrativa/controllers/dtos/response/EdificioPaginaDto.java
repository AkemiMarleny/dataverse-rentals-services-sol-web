package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;

public record EdificioPaginaDto(
        Long id,
        String nombre,
        String direccion,
        Integer numeroPisos) {

    public static EdificioPaginaDto fromEntity(Edificio edificio) {
        return new EdificioPaginaDto(
                edificio.getId(),
                edificio.getNombre(),
                edificio.getDireccion(),
                edificio.getNumeroPisos());
    }

}
