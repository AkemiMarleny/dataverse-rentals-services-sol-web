package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record UpdateToponimoDto(
        @NotBlank(message = "Debe ingresar el nombre del topónimo") @Length(min = 3, message = "El nombre del topónimo debe tener al menos 3 caracteres") String nombre) {
}
