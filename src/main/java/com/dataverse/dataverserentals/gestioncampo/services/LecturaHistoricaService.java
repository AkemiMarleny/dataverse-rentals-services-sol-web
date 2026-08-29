package com.dataverse.dataverserentals.gestioncampo.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.MedidorRepository;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.CreateLecturaHistoricaDto;
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

    public Page<LecturaHistorica> findAll(Long medidorId, Pageable pageable) {

        log.info("Buscando historial de lecturas para el Medidor ID: {}", medidorId);
        return lecturaHistoricaRepository.findByMedidorId(medidorId, pageable);
    }

    public Long create(Long medidorId, @Valid CreateLecturaHistoricaDto payload) throws FieldConstraintException {

        log.info("Procesando registro de lectura para Medidor ID: {}. Datos: {}", medidorId, payload);

        medidorRepository.findById(medidorId)
                .orElseThrow(() -> new FieldConstraintException("medidorId",
                        String.format("El medidor solicitado con el id: %s no se encuentra registrado.",
                                medidorId)));

        LecturaHistorica lecturaHistorica = LecturaHistorica.from(medidorId, payload);
        return lecturaHistoricaRepository.save(lecturaHistorica).getId();
    }

    public LecturaHistorica findById(Long lecturaHistoricaId) throws RecordNotFoundException {

        log.info("Recuperando el registro específico de la lectura histórica con ID: {}", lecturaHistoricaId);
        return lecturaHistoricaRepository.findById(lecturaHistoricaId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("No se encontró la lectura histórica con ID: %d", lecturaHistoricaId)));
    }

}
