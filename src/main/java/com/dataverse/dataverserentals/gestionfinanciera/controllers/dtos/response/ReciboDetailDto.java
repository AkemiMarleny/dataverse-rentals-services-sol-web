package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionfinanciera.entities.Recibo;

public record ReciboDetailDto(
        Long id,
        String codigo,
        Long contratoId,
        Long edificioId,
        String edificioNombre,
        Integer contratoUnidadNumeroPiso,
        String contratoUnidadNumeroPuerta,
        LocalDateTime fechaEmision,
        LocalDateTime fechaVencimiento,
        BigDecimal montoPagado,
        BigDecimal montoTotal,
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