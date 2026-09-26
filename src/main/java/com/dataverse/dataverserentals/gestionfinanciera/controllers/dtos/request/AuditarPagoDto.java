package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.request;

import com.dataverse.dataverserentals.gestionfinanciera.enums.AccionAuditoria;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuditarPagoDto(
        @NotNull
        @Schema(description = "Acción de auditoría (APROBAR o RECHAZAR)", example = "APROBAR", requiredMode = Schema.RequiredMode.REQUIRED)
        AccionAuditoria accion,
        @Size(max = 500)
        @Schema(description = "Motivo del rechazo (requerido cuando se rechaza)", example = "Voucher ilegible")
        String motivoRechazo) {
}