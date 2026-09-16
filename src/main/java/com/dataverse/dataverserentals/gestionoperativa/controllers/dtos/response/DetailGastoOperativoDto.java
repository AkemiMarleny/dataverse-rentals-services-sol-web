package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoGasto;

public record DetailGastoOperativoDto(
        Long id,
        Long edificioId,
        Long categoriaGastoId,
        TipoGasto tipoGastoCode,
        String tipoGastoNombre,
        String descripcion,
        BigDecimal monto,
        LocalDateTime fechaGasto) {

    public static DetailGastoOperativoDto fromEntity(GastoOperativo gastoOperativo) {

        return new DetailGastoOperativoDto(
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
