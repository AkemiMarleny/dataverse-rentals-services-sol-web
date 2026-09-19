package com.dataverse.dataverserentals.gestioncampo.services;

import java.time.LocalDate;

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

        log.info("Búsqueda paginada de lecturas históricas [medidorId: '{}', desde: '{}', hasta: '{}', pageable: {}]", 
                medidorId, fechaDesde, fechaHasta, pageable);

        if (medidorId != null) {
            if (fechaDesde != null && fechaHasta != null) {
                return lecturaHistoricaRepository.findByMedidorIdAndFechaLecturaBetween(
                        medidorId, fechaDesde, fechaHasta, pageable);
            }
            return lecturaHistoricaRepository.findByMedidorId(medidorId, pageable);
        }

        if (fechaDesde != null && fechaHasta != null) {
            return lecturaHistoricaRepository.findByFechaLecturaBetween(fechaDesde, fechaHasta, pageable);
        }

        return lecturaHistoricaRepository.findAll(pageable);
    }

    public Long create(@Valid CreateLecturaHistoricaDto payload, Long usuarioId)
            throws FieldConstraintException {

        log.info("Registrando nueva lectura histórica con los siguientes datos: {}", payload);

        medidorRepository.findById(payload.medidorId())
                .orElseThrow(() -> new FieldConstraintException("medidorId",
                        String.format("El medidor solicitado con el id: %s no se encuentra registrado.",
                                payload.medidorId())));

        LecturaHistorica lecturaHistorica = LecturaHistorica.from(payload.medidorId(), payload, usuarioId);
        return lecturaHistoricaRepository.save(lecturaHistorica).getId();
    }

    public LecturaHistorica findById(Long lecturaHistoricaId) throws RecordNotFoundException {

        log.info("Recuperando datos de la lectura histórica con identificador: {}", lecturaHistoricaId);
        return lecturaHistoricaRepository.findById(lecturaHistoricaId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("No se encontró la lectura histórica con ID: %d",
                                lecturaHistoricaId)));
    }

    public void update(Long lecturaHistoricaId, @Valid UpdateLecturaHistoricaDto payload,
            Long usuarioId)
            throws RecordNotFoundException {

        log.info("Iniciando actualización de la lectura histórica con ID: {}. Datos recibidos: {}", lecturaHistoricaId,
                payload);

        LecturaHistorica lecturaHistorica = lecturaHistoricaRepository
                .findById(lecturaHistoricaId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("No se encontró la lectura histórica con ID: %d",
                                lecturaHistoricaId)));

        lecturaHistorica.updateWith(payload, usuarioId);
        lecturaHistoricaRepository.save(lecturaHistorica);
    }

}
