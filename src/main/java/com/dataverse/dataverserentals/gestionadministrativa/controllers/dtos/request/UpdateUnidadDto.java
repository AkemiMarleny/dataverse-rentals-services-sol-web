package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoAcabado;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateUnidadDto(
        @NotNull(message = "Debe ingresar el número de piso") @Min(value = -1, message = "El número de piso mínimo permitido es -1") @Max(value = 10, message = "El número de piso máximo permitido es 10")
        @Schema(description = "Número de piso (-1 a 10)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer numeroPiso,
        @NotBlank(message = "Debe ingresar el número de puerta") @Length(min = 1)
        @Schema(description = "Número de puerta", example = "501", requiredMode = Schema.RequiredMode.REQUIRED)
        String numeroPuerta,
        @NotNull(message = "Debe ingresar el área en metros cuadrados") @Positive(message = "El área en metros cuadrados debe ser un valor positivo")
        @Schema(description = "Área en metros cuadrados (mayor a cero)", example = "85.50", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal areaMetrosCuadrados,
        @NotNull(message = "Debe ingresar el tipo de acabado")
        @Schema(description = "Tipo de acabado (CASCO_GRIS, BASICO, ESTANDAR, SEMI_LUJO, LUJO)", example = "ESTANDAR", requiredMode = Schema.RequiredMode.REQUIRED)
        TipoAcabado tipoAcabadoCode) {

}