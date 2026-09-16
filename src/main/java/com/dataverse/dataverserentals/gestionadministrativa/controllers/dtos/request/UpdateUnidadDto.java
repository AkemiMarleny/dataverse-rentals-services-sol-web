package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoAcabado;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateUnidadDto(
        @NotNull(message = "Debe ingresar el número de piso") @Min(value = -1, message = "El número de piso mínimo permitido es -1") @Max(value = 10, message = "El número de piso máximo permitido es 10") Integer numeroPiso,
        @NotBlank(message = "Debe ingresar el número de puerta") @Length(min = 1) String numeroPuerta,
        @NotNull(message = "Debe ingresar el área en metros cuadrados") @Positive(message = "El área en metros cuadrados debe ser un valor positivo") BigDecimal areaMetrosCuadrados,
        @NotNull(message = "Debe ingresar el tipo de acabado") TipoAcabado tipoAcabadoCode) {

}
