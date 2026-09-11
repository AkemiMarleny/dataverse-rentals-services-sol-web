package com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.request;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePagoDto(
        @NotNull @Positive BigDecimal montoPagado,
        @NotBlank @Length(min = 3) String observaciones) {
}
