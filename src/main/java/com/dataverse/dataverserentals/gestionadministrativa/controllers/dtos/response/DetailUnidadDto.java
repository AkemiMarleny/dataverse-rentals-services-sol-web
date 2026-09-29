package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoAcabado;

import io.swagger.v3.oas.annotations.media.Schema;

public record DetailUnidadDto(
        @Schema(description = "ID de la unidad", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID del edificio", example = "1")
        Long edificioId,
        @Schema(description = "Número de piso", example = "5")
        Integer numeroPiso,
        @Schema(description = "Número de puerta", example = "501")
        String numeroPuerta,
        @Schema(description = "Área en metros cuadrados", example = "85.50")
        BigDecimal areaMetrosCuadrados,
        @Schema(description = "Tipo de acabado", example = "ESTANDAR")
        TipoAcabado tipoAcabadoCode,
        @Schema(description = "Nombre legible del tipo de acabado", example = "Estándar")
        String tipoAcabadoNombre) {

    public static DetailUnidadDto fromEntity(Unidad unidad) {
        return new DetailUnidadDto(
                unidad.getId(),
                unidad.getEdificioId(),
                unidad.getNumeroPiso(),
                unidad.getNumeroPuerta(),
                unidad.getAreaMetrosCuadrados(),
                unidad.getTipoAcabadoCode(),
                unidad.getTipoAcabadoCode().intoString());
    }
}