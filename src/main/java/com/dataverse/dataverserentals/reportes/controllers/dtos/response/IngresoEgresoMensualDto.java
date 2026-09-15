package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

import java.util.List;

public record IngresoEgresoMensualDto(
        int anio,
        List<IngresoEgresoMensualItemDto> resumen) {
}
