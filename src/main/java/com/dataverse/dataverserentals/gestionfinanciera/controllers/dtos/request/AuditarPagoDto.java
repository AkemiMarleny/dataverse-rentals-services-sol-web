package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.request;

import com.dataverse.dataverserentals.gestionfinanciera.enums.AccionAuditoria;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuditarPagoDto(
        @NotNull AccionAuditoria accion,
        @Size(max = 500) String motivoRechazo) {
}
