package com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateLecturaHistoricaDto(
        @NotNull(message = "El ID del medidor es obligatorio")
        @Schema(description = "ID del medidor", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
        Long medidorId,
        @NotNull(message = "La fecha de lectura es obligatoria") @PastOrPresent(message = "La fecha de lectura no puede ser futura")
        @Schema(description = "Fecha de lectura (no puede ser futura)", example = "2026-01-15", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate fechaLectura,
        @Digits(integer = 8, fraction = 2, message = "El formato numérico es inválido") @PositiveOrZero(message = "El valor de lectura no puede ser negativo")
        @Schema(description = "Valor de la lectura (máximo 8 enteros y 2 decimales, no negativo)", example = "1234.56")
        BigDecimal valorLectura) {
}