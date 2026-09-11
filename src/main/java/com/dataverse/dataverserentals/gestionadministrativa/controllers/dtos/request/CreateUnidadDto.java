package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoAcabado;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateUnidadDto(
        @NotNull @Min(-1) @Max(10) Integer numeroPiso,
        @NotBlank @Length(min = 1) String numeroPuerta,
        @NotNull @Positive BigDecimal areaMetrosCuadrados,
        @NotNull TipoAcabado tipoAcabadoCode) {
}
