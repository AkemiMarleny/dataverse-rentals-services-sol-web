package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoriaGastoDto(
        @NotBlank(message = "Debe indicar el nombre de la categoría") String nombre,
        @NotBlank(message = "Debe indicar o especificar la categoria de gasto") String descripcion) {
}