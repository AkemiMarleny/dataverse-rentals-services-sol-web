package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record CreateToponimoDto(

        @NotBlank(message = "Debe ingresar el nombre del Topónimo") @Length(min = 3, message = "El nombre del Topónimo debe tener al menos 3 caracteres") String nombre) {

}
