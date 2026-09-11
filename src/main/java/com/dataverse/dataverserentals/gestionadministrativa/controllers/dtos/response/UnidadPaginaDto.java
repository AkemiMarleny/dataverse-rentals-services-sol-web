package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoAcabado;

public record UnidadPaginaDto(
        Long id,
        Long edificioId,
        Integer numeroPiso,
        String numeroPuerta,
        BigDecimal areaMetrosCuadrados,
        TipoAcabado tipoAcabadoCode,
        String tipoAcabadoNombre) {

    public static UnidadPaginaDto fromEntity(Unidad unidad) {
        return new UnidadPaginaDto(
                unidad.getId(),
                unidad.getEdificioId(),
                unidad.getNumeroPiso(),
                unidad.getNumeroPuerta(),
                unidad.getAreaMetrosCuadrados(),
                unidad.getTipoAcabadoCode(),
                unidad.getTipoAcabadoCode().intoString());
    }

}
