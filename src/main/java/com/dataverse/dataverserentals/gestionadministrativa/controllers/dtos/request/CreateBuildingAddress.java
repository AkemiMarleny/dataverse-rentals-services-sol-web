package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBuildingAddress(
        @NotNull
        @Schema(description = "ID del topónimo asociado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long toponimoId,
        @NotBlank @Length(min = 3)
        @Schema(description = "Nombre de la vía o referencia (mínimo 3 caracteres)", example = "Av. Los Álamos", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombre,
        @NotBlank @Length(min = 2)
        @Schema(description = "Identificador del inmueble (mínimo 2 caracteres)", example = "MZ-5 LT-12", requiredMode = Schema.RequiredMode.REQUIRED)
        String identificador,
        @NotBlank @Length(min = 3)
        @Schema(description = "Referencia de ubicación (mínimo 3 caracteres)", example = "Frente a la plaza principal", requiredMode = Schema.RequiredMode.REQUIRED)
        String referencia) {

}