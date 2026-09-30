package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;

import io.swagger.v3.oas.annotations.media.Schema;

public record PropuestaLiquidacionDetailsDto(
        @Schema(description = "ID de la propuesta de liquidación", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "Código de la propuesta de liquidación", example = "PROP-ED01-260102-103045")
        String codigo,
        @Schema(description = "ID del edificio", example = "1")
        Long edificioId,
        @Schema(description = "Nombre del edificio", example = "Torre Central")
        String edificioNombre,
        @Schema(description = "Fecha de cálculo de la propuesta")
        LocalDateTime fechaCalculo,
        @Schema(description = "Monto total de la propuesta", example = "1250.00")
        BigDecimal montoTotal,
        @Schema(description = "Estado de la propuesta (PENDIENTE, APROBADO)", example = "PENDIENTE")
        String estadoCode,
        @Schema(description = "Inicio del período facturado")
        LocalDateTime fechaInicioPeriodo,
        @Schema(description = "Fin del período facturado")
        LocalDateTime fechaFinPeriodo,
        @Schema(description = "ID del usuario aprobador (si fue aprobada)", example = "2")
        Optional<Long> aprobadorId,
        @Schema(description = "Nombre de usuario del aprobador (si fue aprobada)", example = "analista1")
        Optional<String> aprobadorNombreUsuario) {

    public static PropuestaLiquidacionDetailsDto fromEntity(PropuestaLiquidacion propuesta) {

        Optional<Usuario> aprobadorOpt = Optional.ofNullable(propuesta.getAprobador());

        return new PropuestaLiquidacionDetailsDto(
                propuesta.getId(),
                propuesta.getCodigo(),
                propuesta.getEdificioId(),
                propuesta.getEdificio().getNombre(),
                propuesta.getFechaCalculo(),
                propuesta.getMontoTotal(),
                propuesta.getEstadoCode().name(),
                propuesta.getFechaInicioPeriodo(),
                propuesta.getFechaFinPeriodo(),
                aprobadorOpt.map(aprobador -> aprobador.getId()),
                aprobadorOpt
                        .map(aprobador -> aprobador.getNombreUsuario()));
    }
}