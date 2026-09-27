package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;

import io.swagger.v3.oas.annotations.media.Schema;

public record DetailEdificioDto(
        @Schema(description = "ID del edificio", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "Nombre del edificio", example = "Torre Central")
        String nombre,
        @Schema(description = "Número de pisos", example = "5")
        Integer numeroPisos) {

    public static DetailEdificioDto fromEntity(Edificio edificio) {
        return new DetailEdificioDto(
                edificio.getId(),
                edificio.getNombre(),
                edificio.getNumeroPisos());
    }

}