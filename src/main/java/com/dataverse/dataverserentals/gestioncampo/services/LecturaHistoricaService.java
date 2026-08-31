package com.dataverse.dataverserentals.gestioncampo.services;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.MedidorRepository;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.CreateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.UpdateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;
import com.dataverse.dataverserentals.gestioncampo.repositories.LecturaHistoricaRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class LecturaHistoricaService {

    private final LecturaHistoricaRepository lecturaHistoricaRepository;
    private final MedidorRepository medidorRepository;

    public Page<LecturaHistorica> findAll(Long medidorId, Pageable pageable,
            LocalDate fechaDesde, LocalDate fechaHasta) {

        log.info("Búsqueda paginada de lecturas históricas [filtro: '{}', pageable: {}]", medidorId, pageable);

        if (fechaDesde != null && fechaHasta != null) {
            return lecturaHistoricaRepository.findByMedidorIdAndFechaLecturaBetween(
                    medidorId, fechaDesde.atStartOfDay(), fechaHasta.atTime(LocalTime.MAX), pageable);
        }

        return lecturaHistoricaRepository.findByMedidorId(medidorId, pageable);
    }

    public Long create(Long medidorId, @Valid CreateLecturaHistoricaDto payload) throws FieldConstraintException {

        log.info("Registrando nueva lectura histórica con los siguientes datos: {}", payload);

        medidorRepository.findById(medidorId)
                .orElseThrow(() -> new FieldConstraintException("medidorId",
                        String.format("El medidor solicitado con el id: %s no se encuentra registrado.",
                                medidorId)));

        LecturaHistorica lecturaHistorica = LecturaHistorica.from(medidorId, payload);
        return lecturaHistoricaRepository.save(lecturaHistorica).getId();
    }

    public LecturaHistorica findById(Long medidorId, Long lecturaHistoricaId) throws RecordNotFoundException {

        log.info("Recuperando datos de la lectura histórica con identificador: {}", lecturaHistoricaId);
        return lecturaHistoricaRepository.findByMedidorIdAndId(medidorId, lecturaHistoricaId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("No se encontró la lectura histórica con ID: %d asociada al medidor con ID: %d",
                                lecturaHistoricaId, medidorId)));
    }

    public void update(Long medidorId, Long lecturaHistoricaId, @Valid UpdateLecturaHistoricaDto payload)
            throws RecordNotFoundException {

        log.info("Iniciando actualización de la lectura histórica con ID: {}. Datos recibidos: {}", lecturaHistoricaId, payload);
        medidorRepository.findById(medidorId)
                .orElseThrow(() -> new FieldConstraintException("medidorId",
                        String.format("El medidor solicitado con el id: %s no se encuentra registrado.",
                                medidorId)));
        LecturaHistorica lecturaHistorica = lecturaHistoricaRepository
                .findByMedidorIdAndId(medidorId, lecturaHistoricaId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("No se encontró la lectura histórica con ID: %d asociada al medidor con ID: %d",
                                lecturaHistoricaId, medidorId)));
        lecturaHistorica.updateWith(payload);
        lecturaHistoricaRepository.save(lecturaHistorica);
    }

}
