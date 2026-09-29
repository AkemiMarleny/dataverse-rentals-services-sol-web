package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dataverse.dataverserentals.gestionoperativa.enums.TipoGasto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateGastoOperativoDto(
        @NotNull(message = "Debe seleccionar una categoría de gasto")
        @Schema(description = "ID de la categoría de gasto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long categoriaGastoId,
        @NotNull(message = "El tipo de gasto es obligatorio")
        @Schema(description = "Tipo de gasto (MATERIAL, MANO_OBRA)", example = "MATERIAL", requiredMode = Schema.RequiredMode.REQUIRED)
        TipoGasto tipoGastoCode,
        @NotBlank(message = "Debe indicar o especificar el detalle del gasto operativo")
        @Schema(description = "Descripción o detalle del gasto", example = "Reparación de ascensor", requiredMode = Schema.RequiredMode.REQUIRED)
        String descripcion,
        @NotNull(message = "El monto es obligatorio") @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
        @Schema(description = "Monto del gasto (mayor a 0)", example = "350.00", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal monto,
        @NotNull(message = "La fecha del gasto es obligatoria")
        @Schema(description = "Fecha del gasto (yyyy-MM-dd)", example = "2026-01-15", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate fechaGasto) {
}