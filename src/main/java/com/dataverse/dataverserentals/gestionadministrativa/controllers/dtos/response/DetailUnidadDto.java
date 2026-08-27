package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoAcabado;

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
