package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MorosidadItemDto(
        @Schema(description = "Código numérico del mes (1-12)", example = "1")
        int mesCodigo,
        @Schema(description = "Nombre del mes", example = "ENERO")
        String mesNombre,
        @Schema(description = "Pagos con 1 a 30 días de atraso", example = "10")
        long pagos1a30Dias,
        @Schema(description = "Pagos con 31 a 60 días de atraso", example = "5")
        long pagos31a60Dias,
        @Schema(description = "Pagos con más de 60 días de atraso", example = "3")
        long pagosMas60Dias) {
}