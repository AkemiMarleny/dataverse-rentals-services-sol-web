package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreateContratoDto(
        @NotNull
        @Schema(description = "ID de la unidad arrendada", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
        Long unidadId,
        @NotNull
        @Schema(description = "ID de la persona arrendataria", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        Long personaId,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Schema(description = "Fecha de inicio del contrato (yyyy-MM-dd)", example = "2026-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate fechaInicio,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Schema(description = "Fecha de fin del contrato (yyyy-MM-dd)", example = "2026-12-31", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate fechaFin,
        @NotNull @DecimalMin("0.01")
        @Schema(description = "Alquiler mensual (mayor a 0.01)", example = "1200.00", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal alquilerMensual) {
}