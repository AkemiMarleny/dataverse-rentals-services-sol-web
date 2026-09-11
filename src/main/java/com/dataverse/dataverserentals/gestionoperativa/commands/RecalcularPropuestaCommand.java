package com.dataverse.dataverserentals.gestionoperativa.commands;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.EdificioRepository;
import com.dataverse.dataverserentals.gestionoperativa.entities.DetallePropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.enums.EstadoPropuesta;
import com.dataverse.dataverserentals.gestionoperativa.repositories.DetallePropuestaLiquidacionRepository;
import com.dataverse.dataverserentals.gestionoperativa.repositories.PropuestaLiquidacionRepository;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecalcularPropuestaCommand {

    private final SecurityContext securityContext;
    private final PropuestaLiquidacionRepository propuestaLiquidacionRepository;
    private final DetallePropuestaLiquidacionRepository detallePropuestaLiquidacionRepository;
    private final EdificioRepository edificioRepository;

    private final CalcularDetallePropuestaGastosComunesEdificioCommand calcularDetallePropuestaGastosComunesEdificioCommand;
    private final CalcularDetallePropuestaEdificioElectricidadCommand calcularDetallePropuestaEdificioElectricidadCommand;
    private final CalcularDetallePropuestaEdificioAguaCommand calcularDetallePropuestaEdificioAguaCommand;

    @Transactional(rollbackFor = Exception.class)
    public void execute(Long propuestaId) throws RecordNotFoundException, UnprocesableEntity {
        PropuestaLiquidacion propuesta = propuestaLiquidacionRepository.findById(propuestaId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Propuesta de liquidación no encontrada con id: " + propuestaId));

        if (!EstadoPropuesta.PENDIENTE.equals(propuesta.getEstadoCode())) {
            throw new UnprocesableEntity("Solo se pueden recalcular propuestas en estado PENDIENTE");
        }

        Edificio edificio = edificioRepository.findById(propuesta.getEdificioId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "Edificio no encontrado con id: " + propuesta.getEdificioId()));

        log.info("recalcular propuesta de liquidacion id: {}, edificioId: {}, edificioNombre: {}",
                propuesta.getId(), edificio.getId(), edificio.getNombre());

        LocalDateTime fechaCalculo = LocalDateTime.now();

        detallePropuestaLiquidacionRepository.deleteByPropuestaLiquidacionId(propuesta.getId());

        propuesta.setDetalles(new java.util.ArrayList<>());
        propuesta.setMontoTotal(java.math.BigDecimal.ZERO);
        propuesta.setFechaCalculo(fechaCalculo);

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

        log.info("propuesta recalculada exitosamente id: {}, montoTotal: {}",
                propuesta.getId(), propuesta.getMontoTotal());
    }
}
