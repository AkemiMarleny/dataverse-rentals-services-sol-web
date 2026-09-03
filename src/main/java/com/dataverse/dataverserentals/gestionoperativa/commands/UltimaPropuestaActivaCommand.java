package com.dataverse.dataverserentals.gestionoperativa.commands;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.enums.EstadoPropuesta;
import com.dataverse.dataverserentals.gestionoperativa.repositories.PropuestaLiquidacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UltimaPropuestaActivaCommand {

    private final PropuestaLiquidacionRepository propuestaLiquidacionRepository;

    public Optional<PropuestaLiquidacion> execute(
            Edificio edificio,
            LocalDateTime periodoInicio,
            LocalDateTime periodoFin) {

        Optional<PropuestaLiquidacion> ultimaPropuestaLiquidacionAprobada = propuestaLiquidacionRepository
                .findFirstByEdificioAndEstadoAndPeriodo(
                        edificio.getId(),
                        EstadoPropuesta.APROBADO,
                        periodoInicio,
                        periodoFin);

        return ultimaPropuestaLiquidacionAprobada;
    }
}
