package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.request;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePagoDto(
        @NotNull @Positive
        @Schema(description = "Monto pagado (mayor a cero)", example = "1200.00", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal montoPagado,
        @NotBlank @Length(min = 3)
        @Schema(description = "Observaciones del pago (mínimo 3 caracteres)", example = "Transferencia bancaria", requiredMode = Schema.RequiredMode.REQUIRED)
        String observaciones) {
}