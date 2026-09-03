package com.dataverse.dataverserentals.gestionfinanciera.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Contrato;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.ContratoRepository;
import com.dataverse.dataverserentals.gestionoperativa.commands.UltimaPropuestaActivaCommand;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerarRecibosEdificioCommand {

    private final UltimaPropuestaActivaCommand ultimaPropuestaActivaCommand;
    private final CalcularAreaArrendableEdificioCommand calcularAreaArrendableEdificioCommand;
    private final GenerarReciboContratoCommand generarReciboContratoCommand;
    private final ContratoRepository contratoRepository;

    @Transactional(rollbackFor = Exception.class)
    public void execute(Edificio edificio, UsuarioPrincipal usuarioPrincipal) {
        log.info("generando recibos para el edificioId: {}, edificioNombre: {}",
                edificio.getId(),
                edificio.getNombre());

        LocalDateTime fechaCalculo = LocalDateTime.now();
        LocalDateTime fechaInicioPeriodo = fechaCalculo.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime fechaFinPeriodo = fechaCalculo.with(TemporalAdjusters.lastDayOfMonth());

        log.info("fechaCalculo: {}, fechaInicioPeriodo: {}, fechaFinPeriodo: {}",
                fechaCalculo,
                fechaInicioPeriodo,
                fechaFinPeriodo);

        Optional<PropuestaLiquidacion> propuestaActivaPeriodo = ultimaPropuestaActivaCommand.execute(
                edificio, fechaInicioPeriodo, fechaFinPeriodo);
        if (propuestaActivaPeriodo.isEmpty()) {
            log.error(
                    "no existe propuesta de liquidación aprobada edificioId: {}, fechaInicioPeriodo: {}, fechaFinPeriodo: {}",
                    edificio.getId(),
                    fechaInicioPeriodo,
                    fechaFinPeriodo);
            return;
        }

        BigDecimal areaArrendableEdificio = calcularAreaArrendableEdificioCommand.execute(edificio);
        log.info("edificioId: {}, areaArrendableEdificio: {}", edificio.getId(), areaArrendableEdificio);

        List<Contrato> contratos = contratoRepository.findAllActivosNoVencidos(
                fechaInicioPeriodo);
        log.info("generando recibos para contratos [cantidadContratos={}]", contratos.size());

        contratos.stream().parallel().forEach(contrato -> {
            generarReciboContratoCommand.execute(
                    usuarioPrincipal,
                    propuestaActivaPeriodo.get(),
                    contrato,
                    areaArrendableEdificio,
                    fechaCalculo,
                    fechaInicioPeriodo,
                    fechaFinPeriodo);
        });

    }
}
