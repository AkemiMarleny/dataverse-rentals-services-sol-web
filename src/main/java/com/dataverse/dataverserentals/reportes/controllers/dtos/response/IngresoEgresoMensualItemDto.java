package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record IngresoEgresoMensualItemDto(
        @Schema(description = "Monto total de ingresos del mes", example = "25000.00")
        BigDecimal montoIngreso,
        @Schema(description = "Monto total de egresos del mes", example = "12000.00")
        BigDecimal montoEgreso,
        @Schema(description = "Código numérico del mes (1-12)", example = "1")
        int mesCodigo,
        @Schema(description = "Nombre del mes", example = "ENERO")
        String mesNombre) {

}