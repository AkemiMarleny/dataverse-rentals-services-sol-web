package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Toponimo;

import io.swagger.v3.oas.annotations.media.Schema;

public record ToponimoPaginaDto(
        @Schema(description = "ID del topónimo", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "Nombre del topónimo", example = "Av. Los Álamos")
        String nombre) {

    public static ToponimoPaginaDto fromEntity(Toponimo toponimo) {
        return new ToponimoPaginaDto(
                toponimo.getId(),
                toponimo.getNombre());
    }

}