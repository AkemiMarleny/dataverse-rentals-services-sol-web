package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

import java.util.List;

public record MorosidadDto(
        int anio,
        long totalPagosMorosos,
        double promedio1a30,
        double promedio31a60,
        double promedioMas60,
        List<MorosidadItemDto> resumen) {
}
