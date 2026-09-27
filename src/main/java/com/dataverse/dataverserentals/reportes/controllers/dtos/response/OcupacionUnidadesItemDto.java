package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record OcupacionUnidadesItemDto(
        @Schema(description = "Código numérico del mes (1-12)", example = "1")
        int mesCodigo,
        @Schema(description = "Nombre del mes", example = "ENERO")
        String mesNombre,
        @Schema(description = "Unidades ocupadas", example = "98")
        long unidadesOcupadas,
        @Schema(description = "Unidades disponibles", example = "22")
        long unidadesDisponibles) {
}