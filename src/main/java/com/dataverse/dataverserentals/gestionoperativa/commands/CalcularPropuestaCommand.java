package com.dataverse.dataverserentals.gestionoperativa.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionoperativa.entities.DetallePropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.enums.EstadoPropuesta;
import com.dataverse.dataverserentals.gestionoperativa.repositories.DetallePropuestaLiquidacionRepository;
import com.dataverse.dataverserentals.gestionoperativa.repositories.PropuestaLiquidacionRepository;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CalcularPropuestaCommand {

    private final SecurityContext securityContext;

    private final PropuestaLiquidacionRepository propuestaLiquidacionRepository;
    private final DetallePropuestaLiquidacionRepository detallePropuestaLiquidacionRepository;

    private final CalcularDetallePropuestaGastosComunesEdificioCommand calcularDetallePropuestaGastosComunesEdificioCommand;
    private final CalcularDetallePropuestaEdificioElectricidadCommand calcularDetallePropuestaEdificioElectricidadCommand;
    private final CalcularDetallePropuestaEdificioAguaCommand calcularDetallePropuestaEdificioAguaCommand;

    @Transactional(rollbackFor = Exception.class)
    public void execute(Edificio edificio) {
        log.info("generando propuesta de liquidacion para el edificio con id: {}, nombre: {}",
                edificio.getId(),
                edificio.getNombre());

        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();

        LocalDateTime fechaCalculo = LocalDateTime.now();
        LocalDateTime fechaInicioPeriodo = fechaCalculo.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime fechaFinPeriodo = fechaCalculo.with(TemporalAdjusters.lastDayOfMonth());

        PropuestaLiquidacion propuesta = PropuestaLiquidacion
                .builder()
                .edificioId(edificio.getId())
                .fechaCalculo(fechaCalculo)
                .montoTotal(new BigDecimal(0.0))
                .estadoCode(EstadoPropuesta.PENDIENTE)
                .fechaInicioPeriodo(fechaInicioPeriodo)
                .fechaFinPeriodo(fechaFinPeriodo)
                .fechaCreacion(fechaCalculo)
                .usuarioCreacionId(usuarioPrincipal.usuarioId())
                .build();

        propuesta = propuestaLiquidacionRepository.save(propuesta);

        DetallePropuestaLiquidacion detalleGastosComunes = calcularDetallePropuestaGastosComunesEdificioCommand.execute(
                edificio, propuesta);
        propuesta.agregarDetalle(detalleGastosComunes);

        Optional<DetallePropuestaLiquidacion> detalleExcedenteElectricidad = calcularDetallePropuestaEdificioElectricidadCommand
                .execute(edificio, propuesta);
        if (detalleExcedenteElectricidad.isPresent()) {
            propuesta.agregarDetalle(detalleExcedenteElectricidad.get());
        }

        Optional<DetallePropuestaLiquidacion> detalleExcedenteAgua = calcularDetallePropuestaEdificioAguaCommand
                .execute(edificio, propuesta);
        if (detalleExcedenteAgua.isPresent()) {
            propuesta.agregarDetalle(detalleExcedenteAgua.get());
        }

        propuestaLiquidacionRepository.save(propuesta);
        detallePropuestaLiquidacionRepository.saveAll(propuesta.getDetalles());
    }
}
