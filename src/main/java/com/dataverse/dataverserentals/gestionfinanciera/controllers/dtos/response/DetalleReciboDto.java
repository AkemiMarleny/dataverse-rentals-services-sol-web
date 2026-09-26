package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.gestionfinanciera.entities.DetalleRecibo;

import io.swagger.v3.oas.annotations.media.Schema;

public record DetalleReciboDto(
        @Schema(description = "ID del detalle", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID del recibo al que pertenece", example = "1")
        Long reciboId,
        @Schema(description = "Código del tipo de concepto", example = "ALQUILER_MENSUAL")
        String tipoConceptoCodigo,
        @Schema(description = "Nombre del tipo de concepto", example = "Alquiler mensual")
        String tipoConceptoNombre,
        @Schema(description = "Descripción del concepto", example = "Alquiler mensual de la unidad 501")
        String descripcion,
        @Schema(description = "Monto del concepto", example = "1200.00")
        BigDecimal monto) {

    public static DetalleReciboDto fromEntity(DetalleRecibo detalleRecibo) {
        return new DetalleReciboDto(
                detalleRecibo.getId(),
                detalleRecibo.getReciboId(),
                detalleRecibo.getTipoConcepto().name(),
                detalleRecibo.getTipoConcepto().intoString(),
                detalleRecibo.getDescripcion(),
                detalleRecibo.getMonto());
    }
}