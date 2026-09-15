package com.dataverse.dataverserentals.reportes.controllers.dtos.response;

import java.math.BigDecimal;

public record IngresoEgresoMensualItemDto(
        BigDecimal montoIngreso,
        BigDecimal montoEgreso,
        int mesCodigo,
        String mesNombre) {

}
