package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateLecturaHistoricaDto(
        @NotNull Long medidorId,
        @Digits(integer = 8, fraction = 2, message = "El formato numérico es inválido") @PositiveOrZero(message = "El valor de lectura no puede ser negativo") BigDecimal valorLectura,
        @NotNull Long usuarioCreacionId) {
}
