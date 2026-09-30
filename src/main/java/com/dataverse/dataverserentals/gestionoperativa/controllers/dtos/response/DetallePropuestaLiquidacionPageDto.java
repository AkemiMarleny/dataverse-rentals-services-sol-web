package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.gestionoperativa.entities.DetallePropuestaLiquidacion;

import io.swagger.v3.oas.annotations.media.Schema;

public record DetallePropuestaLiquidacionPageDto(
        @Schema(description = "ID del detalle", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID de la propuesta de liquidación", example = "1")
        Long propuestaLiquidacionId,
        @Schema(description = "Código del tipo de concepto (GASTOS_COMUNES, ELECTRICIDAD_GASTOS_COMUNES, AGUA_GASTOS_COMUNES)", example = "GASTOS_COMUNES")
        String tipoConceptoCodigo,
        @Schema(description = "Nombre del tipo de concepto", example = "Gastos comunes")
        String tipoConceptoNombre,
        @Schema(description = "Descripción del concepto", example = "Gastos comunes del período de enero")
        String descripcion,
        @Schema(description = "Monto del concepto", example = "850.00")
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