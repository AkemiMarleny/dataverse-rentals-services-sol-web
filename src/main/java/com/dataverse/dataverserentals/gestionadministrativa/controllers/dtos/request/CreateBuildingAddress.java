package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBuildingAddress(
        @NotNull Long toponimoId,
        @NotBlank @Length(min = 3) String nombre,
        @NotBlank @Length(min = 2) String identificador,
        @NotBlank @Length(min = 3) String referencia) {

}
