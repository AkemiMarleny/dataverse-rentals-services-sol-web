package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionfinanciera.entities.Pago;

import io.swagger.v3.oas.annotations.media.Schema;

public record PagoDto(
        @Schema(description = "ID del pago", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID del recibo asociado", example = "1")
        Long reciboId,
        @Schema(description = "ID del usuario validador", example = "2")
        Long validadorId,
        @Schema(description = "Nombre de usuario del validador", example = "analista1")
        String validadorNombreUsuario,
        @Schema(description = "Monto pagado", example = "1200.00")
        BigDecimal montoPagado,
        @Schema(description = "URL del voucher adjunto", example = "https://bucket.s3.amazonaws.com/vouchers/1/pago.pdf")
        String urlVoucher,
        @Schema(description = "Observaciones del pago", example = "Transferencia bancaria")
        String observaciones,
        @Schema(description = "Estado del pago (PENDIENTE, APROBADO, RECHAZADO)", example = "PENDIENTE")
        String estadoCodigo,
        @Schema(description = "Fecha de aprobación")
        LocalDateTime fechaAprobacion,
        @Schema(description = "Fecha de rechazo")
        LocalDateTime fechaRechazo,
        @Schema(description = "Motivo del rechazo", example = "Voucher ilegible")
        String motivoRechazo,
        @Schema(description = "Fecha de creación del pago")
        LocalDateTime fechaCreacion) {

    public static PagoDto fromEntity(Pago pago) {
        var validador = Optional.ofNullable(pago.getValidador());

        return new PagoDto(
                pago.getId(),
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