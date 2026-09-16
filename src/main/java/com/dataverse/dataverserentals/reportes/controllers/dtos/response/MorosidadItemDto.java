package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

public record MorosidadItemDto(
        int mesCodigo,
        String mesNombre,
        long pagos1a30Dias,
        long pagos31a60Dias,
        long pagosMas60Dias) {
}
