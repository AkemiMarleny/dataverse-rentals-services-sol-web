package com.dataverse.dataverserentals.gestionoperativa.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.gestionoperativa.commands.CalcularPropuestasCommand;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.repositories.PropuestaLiquidacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropuestaLiquidacionService {
    private final CalcularPropuestasCommand calcularPropuestasCommand;
    private final PropuestaLiquidacionRepository propuestaLiquidacionRepository;

    public void calcularPropuestas() throws AccessDeniedException {
        calcularPropuestasCommand.execute();
    }

    public Page<PropuestaLiquidacion> findAllPropuestasLiquidacion(
            Pageable pageable,
            Optional<Long> edificioId,
            Optional<LocalDate> fechaDesde,
            Optional<LocalDate> fechaHasta) {

        return propuestaLiquidacionRepository.findAllWithFilters(
                edificioId,
                fechaDesde.map(desde -> desde.atTime(LocalTime.MIN)),
                fechaHasta.map(hasta -> hasta.atTime(LocalTime.MAX)),
                pageable);
    }
}
