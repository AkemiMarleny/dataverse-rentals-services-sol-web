package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

public record OcupacionUnidadesItemDto(
        int mesCodigo,
        String mesNombre,
        long unidadesOcupadas,
        long unidadesDisponibles) {
}
