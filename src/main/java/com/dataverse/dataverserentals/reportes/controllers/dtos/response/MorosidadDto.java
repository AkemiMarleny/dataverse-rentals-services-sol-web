package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record MorosidadDto(
        @Schema(description = "Año del reporte", example = "2026")
        int anio,
        @Schema(description = "Total de pagos morosos", example = "45")
        long totalPagosMorosos,
        @Schema(description = "Promedio de días de atraso en el rango de 1 a 30 días", example = "12.5")
        double promedio1a30,
        @Schema(description = "Promedio de días de atraso en el rango de 31 a 60 días", example = "41.2")
        double promedio31a60,
        @Schema(description = "Promedio de días de atraso en el rango de más de 60 días", example = "85.7")
        double promedioMas60,
        @Schema(description = "Resumen mensual de morosidad")
        List<MorosidadItemDto> resumen) {
}