package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoGasto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DetailGastoOperativoDto(
        @Schema(description = "ID del gasto operativo", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID del edificio", example = "1")
        Long edificioId,
        @Schema(description = "ID de la categoría de gasto", example = "1")
        Long categoriaGastoId,
        @Schema(description = "Nombre de la categoría de gasto", example = "Mantenimiento")
        String categoriaGastoNombre,
        @Schema(description = "Tipo de gasto (MATERIAL, MANO_OBRA)", example = "MATERIAL")
        TipoGasto tipoGastoCode,
        @Schema(description = "Nombre legible del tipo de gasto", example = "Material")
        String tipoGastoNombre,
        @Schema(description = "Descripción o detalle del gasto", example = "Reparación de ascensor")
        String descripcion,
        @Schema(description = "Monto del gasto", example = "350.00")
        BigDecimal monto,
        @Schema(description = "Fecha del gasto", example = "2026-01-15")
        LocalDate fechaGasto) {

    public static DetailGastoOperativoDto fromEntity(GastoOperativo gastoOperativo) {

        return new DetailGastoOperativoDto(
                gastoOperativo.getId(),
                gastoOperativo.getEdificioId(),
                gastoOperativo.getCategoriaGastoId(),
                gastoOperativo.getCategoriaGasto().getNombre(),
                gastoOperativo.getTipoGastoCode(),
                gastoOperativo.getTipoGastoCode().intoString(),
                gastoOperativo.getDescripcion(),
                gastoOperativo.getMonto(),
                gastoOperativo.getFechaGasto());
    }
}