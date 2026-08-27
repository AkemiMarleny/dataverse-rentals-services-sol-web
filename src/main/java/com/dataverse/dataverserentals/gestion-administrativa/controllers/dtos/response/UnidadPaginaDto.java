package com.dataverse.dataverserentals.administrations.controllers.dtos.response;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.administrations.entities.Unidad;
import com.dataverse.dataverserentals.administrations.enums.TipoAcabado;

public record UnidadPaginaDto(
        Long id,
        Long edificioId,
        String nombreEdificio,
        Integer numeroPiso,
        String numeroPuerta,
        BigDecimal areaMetrosCuadrados,
        TipoAcabado tipoAcabado) {

    public static UnidadPaginaDto fromEntity(Unidad unidad) {
        return new UnidadPaginaDto(
                unidad.getId(),
                unidad.getEdificioId(),
                unidad.getEdificio().getNombre(),
                unidad.getNumeroPiso(),
                unidad.getNumeroPuerta(),
                unidad.getAreaMetrosCuadrados(),
                unidad.getTipoAcabado());
    }

}
