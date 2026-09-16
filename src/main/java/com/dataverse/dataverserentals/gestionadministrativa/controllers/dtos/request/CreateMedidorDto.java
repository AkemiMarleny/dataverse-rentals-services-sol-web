package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMedidorDto(
        @NotNull(message = "Debe ingresar el tipo de medidor") TipoMedidor tipoMedidorCode,
        @NotBlank(message = "Debe ingresar el número de serie") @Length(min = 7) String numeroSerie) {
}
