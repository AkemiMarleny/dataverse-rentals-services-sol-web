package com.dataverse.dataverserentals.administrations.controllers.dtos.response;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.administrations.entities.Unidad;
import com.dataverse.dataverserentals.administrations.enums.TipoAcabado;

public record DetailUnidadDto(
        Long id,
        Long edificioId,
        String nombreEdificio,
        Integer numeroPiso,
        String numeroPuerta,
        BigDecimal areaMetrosCuadrados,
        TipoAcabado tipoAcabado) {

    public static DetailUnidadDto fromEntity(Unidad unidad) {
        return new DetailUnidadDto(
                unidad.getId(),
                unidad.getEdificioId(),
                unidad.getEdificio().getNombre(),
                unidad.getNumeroPiso(),
                unidad.getNumeroPuerta(),
                unidad.getAreaMetrosCuadrados(),
                unidad.getTipoAcabado());
    }
}
