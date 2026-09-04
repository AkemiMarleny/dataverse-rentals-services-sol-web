package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMedidorDto(
        @NotNull TipoMedidor tipoMedidorCode,
        @NotBlank @Length(min = 7) String numeroSerie) {
}
