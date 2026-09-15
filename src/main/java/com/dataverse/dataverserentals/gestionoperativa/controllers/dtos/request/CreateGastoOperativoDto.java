package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionoperativa.enums.TipoGasto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateGastoOperativoDto(
        @NotNull(message = "Debe seleccionar un edificio") Long edificioId,
        @NotNull(message = "Debe seleccionar una categoría de gasto") Long categoriaGastoId,
        @NotNull(message = "El tipo de gasto es obligatorio") TipoGasto tipoGastoCode,
        @NotBlank(message = "Debe indicar o especificar el detalle del gasto operativo") String descripcion,
        @NotNull(message = "El monto es obligatorio") @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0") BigDecimal monto,
        @NotNull(message = "La fecha del gasto es obligatoria") LocalDateTime fechaGasto) {
}