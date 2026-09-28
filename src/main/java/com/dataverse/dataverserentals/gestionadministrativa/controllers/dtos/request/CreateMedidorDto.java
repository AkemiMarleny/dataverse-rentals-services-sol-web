package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMedidorDto(
        @NotNull(message = "Debe ingresar el tipo de medidor")
        @Schema(description = "Tipo de medidor (GENERAL_ELECTRICIDAD, GENERAL_AGUA, ELECTRICIDAD, AGUA)", example = "ELECTRICIDAD", requiredMode = Schema.RequiredMode.REQUIRED)
        TipoMedidor tipoMedidorCode,
        @NotBlank(message = "Debe ingresar el número de serie") @Length(min = 7)
        @Schema(description = "Número de serie (mínimo 7 caracteres)", example = "ELEC-2026-001", requiredMode = Schema.RequiredMode.REQUIRED)
        String numeroSerie) {
}