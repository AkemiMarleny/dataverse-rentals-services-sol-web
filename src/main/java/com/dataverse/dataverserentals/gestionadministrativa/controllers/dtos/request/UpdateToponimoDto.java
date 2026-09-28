package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

public record UpdateToponimoDto(
        @NotBlank(message = "Debe ingresar el nombre del topónimo") @Length(min = 3, message = "El nombre del topónimo debe tener al menos 3 caracteres")
        @Schema(description = "Nombre del topónimo (mínimo 3 caracteres)", example = "Av. Los Álamos", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombre) {
}