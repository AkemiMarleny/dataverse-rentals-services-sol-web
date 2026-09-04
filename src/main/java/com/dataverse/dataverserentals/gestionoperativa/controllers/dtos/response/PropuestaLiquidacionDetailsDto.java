package com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;

public record PropuestaLiquidacionDetailsDto(
        Long id,
        String codigo,
        Long edificioId,
        String edificioNombre,
        LocalDateTime fechaCalculo,
        BigDecimal montoTotal,
        String estadoCode,
        LocalDateTime fechaInicioPeriodo,
        LocalDateTime fechaFinPeriodo,
        Optional<Long> aprobadorId,
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