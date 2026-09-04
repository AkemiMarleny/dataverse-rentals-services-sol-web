package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.gestionoperativa.entities.DetallePropuestaLiquidacion;

public record DetallePropuestaLiquidacionPageDto(
        Long id,
        Long propuestaLiquidacionId,
        String tipoConceptoCodigo,
        String tipoConceptoNombre,
        String descripcion,
        BigDecimal monto) {

    public static DetallePropuestaLiquidacionPageDto fromEntity(DetallePropuestaLiquidacion detalle) {
        return new DetallePropuestaLiquidacionPageDto(
                detalle.getId(),
                detalle.getPropuestaLiquidacionId(),
                detalle.getTipoConceptoPropuesta().name(),
                detalle.getTipoConceptoPropuesta().intoString(),
                detalle.getDescripcion(),
                detalle.getMonto());
    }
}