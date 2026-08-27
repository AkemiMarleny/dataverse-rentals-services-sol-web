package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoAcabado;

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
