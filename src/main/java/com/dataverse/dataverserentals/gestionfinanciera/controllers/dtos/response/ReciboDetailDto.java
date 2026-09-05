package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionfinanciera.entities.Recibo;

public record ReciboDetailDto(
        Long id,
        String codigo,
        Long contratoId,
        LocalDateTime fechaEmision,
        LocalDateTime fechaVencimiento,
        BigDecimal montoTotal,
        String estadoCode) {

    public static ReciboDetailDto fromEntity(Recibo recibo) {
        return new ReciboDetailDto(
                recibo.getId(),
                recibo.getCodigo(),
                recibo.getContratoId(),
                recibo.getFechaEmision(),
                recibo.getFechaVencimiento(),
                recibo.getMontoTotal(),
                recibo.getEstadoCode().name());
    }
}