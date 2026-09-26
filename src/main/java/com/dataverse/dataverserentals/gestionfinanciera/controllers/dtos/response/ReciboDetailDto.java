package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionfinanciera.entities.Recibo;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReciboDetailDto(
        @Schema(description = "ID del recibo", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "Código del recibo", example = "C003-REC-260102-103045")
        String codigo,
        @Schema(description = "ID del contrato asociado", example = "3")
        Long contratoId,
        @Schema(description = "ID del edificio", example = "1")
        Long edificioId,
        @Schema(description = "Nombre del edificio", example = "Torre Central")
        String edificioNombre,
        @Schema(description = "Número de piso de la unidad", example = "5")
        Integer contratoUnidadNumeroPiso,
        @Schema(description = "Número de puerta de la unidad", example = "501")
        String contratoUnidadNumeroPuerta,
        @Schema(description = "Fecha de emisión del recibo")
        LocalDateTime fechaEmision,
        @Schema(description = "Fecha de vencimiento del recibo")
        LocalDateTime fechaVencimiento,
        @Schema(description = "Monto pagado a la fecha", example = "500.00")
        BigDecimal montoPagado,
        @Schema(description = "Monto total del recibo", example = "1200.00")
        BigDecimal montoTotal,
        @Schema(description = "Estado del recibo (PENDIENTE, PARCIALMENTE_PAGADO, ABONADO)", example = "PARCIALMENTE_PAGADO")
        String estadoCode) {

    public static ReciboDetailDto fromEntity(Recibo recibo) {
        return new ReciboDetailDto(
                recibo.getId(),
                recibo.getCodigo(),
                recibo.getContratoId(),
                recibo.getContrato().getUnidad().getEdificio().getId(),
                recibo.getContrato().getUnidad().getEdificio().getNombre(),
                recibo.getContrato().getUnidad().getNumeroPiso(),
                recibo.getContrato().getUnidad().getNumeroPuerta(),
                recibo.getFechaEmision(),
                recibo.getFechaVencimiento(),
                recibo.getMontoPagado(),
                recibo.getMontoTotal(),
                recibo.getEstadoCode().name());
    }
}