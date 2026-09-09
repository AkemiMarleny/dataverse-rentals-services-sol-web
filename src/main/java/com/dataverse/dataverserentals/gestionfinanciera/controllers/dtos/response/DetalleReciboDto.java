package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.gestionfinanciera.entities.DetalleRecibo;

public record DetalleReciboDto(
        Long id,
        Long reciboId,
        String tipoConceptoCodigo,
        String tipoConceptoNombre,
        String descripcion,
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
