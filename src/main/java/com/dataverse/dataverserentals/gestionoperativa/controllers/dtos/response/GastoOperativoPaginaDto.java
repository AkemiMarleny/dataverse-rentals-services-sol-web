package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoGasto;

public record GastoOperativoPaginaDto(
        Long id,
        Long edificioId,
        Long categoriaGastoId,
        TipoGasto tipoGastoCode,
        String tipoGastoNombre,
        String descripcion,
        BigDecimal monto,
        LocalDate fechaGasto) {

    public static GastoOperativoPaginaDto fromEntity(GastoOperativo gastoOperativo) {

        return new GastoOperativoPaginaDto(
                gastoOperativo.getId(),
                gastoOperativo.getEdificioId(),
                gastoOperativo.getCategoriaGastoId(),
                gastoOperativo.getTipoGastoCode(),
                gastoOperativo.getTipoGastoCode().intoString(),
                gastoOperativo.getDescripcion(),
                gastoOperativo.getMonto(),
                gastoOperativo.getFechaGasto());
    }
}