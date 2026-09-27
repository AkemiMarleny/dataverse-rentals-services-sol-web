package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record OcupacionUnidadesDto(
        @Schema(description = "Año del reporte", example = "2026")
        int anio,
        @Schema(description = "Total de unidades", example = "120")
        long totalUnidades,
        @Schema(description = "Promedio de ocupación porcentual", example = "82.5")
        double promedioOcupacion,
        @Schema(description = "Resumen mensual de ocupación")
        List<OcupacionUnidadesItemDto> resumen) {
}