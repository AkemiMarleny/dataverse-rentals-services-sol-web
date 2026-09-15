package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

import java.util.List;

public record OcupacionUnidadesDto(
        int anio,
        long totalUnidades,
        double promedioOcupacion,
        List<OcupacionUnidadesItemDto> resumen) {
}
