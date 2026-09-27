package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record IngresoEgresoMensualDto(
        @Schema(description = "Año del resumen", example = "2026")
        int anio,
        @Schema(description = "Resumen mensual de ingresos y egresos")
        List<IngresoEgresoMensualItemDto> resumen) {
}