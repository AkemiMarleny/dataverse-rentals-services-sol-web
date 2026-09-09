package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionfinanciera.entities.Pago;

public record PagoDto(
        Long reciboId,
        Long validadorId,
        String validadorNombreUsuario,
        BigDecimal montoPagado,
        String urlVoucher,
        String observaciones,
        String estadoCodigo,
        LocalDateTime fechaAprobacion,
        LocalDateTime fechaRechazo,
        String motivoRechazo,
        LocalDateTime fechaCreacion) {

    public static PagoDto fromEntity(Pago pago) {
        var validador = Optional.ofNullable(pago.getValidador());

        return new PagoDto(
                pago.getReciboId(),
                pago.getValidadorId(),
                validador.map(Usuario::getNombreUsuario).orElse(null),
                pago.getMontoPagado(),
                pago.getUrlVoucher(),
                pago.getObservaciones(),
                pago.getEstadoCodigo() != null ? pago.getEstadoCodigo().name() : null,
                pago.getFechaAprobacion(),
                pago.getFechaRechazo(),
                pago.getMotivoRechazo(),
                pago.getFechaCreacion());
    }
}
