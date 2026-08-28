package com.dataverse.dataverserentals.gestionadministrativa.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.LecturaHistorica;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.LecturaHistoricaRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.MedidorRepository;

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

    public Long create(@Valid CreateLecturaHistoricaDto payload) throws FieldConstraintException {

        log.info("Procesando registro de lectura para Medidor ID: {}. Datos: {}", payload.medidorId(), payload);

        medidorRepository.findById(payload.medidorId())
                .orElseThrow(() -> new FieldConstraintException("medidorId",
                        String.format("El medidor solicitado con el id: %s no se encuentra registrado.",
                                payload.medidorId())));

        LecturaHistorica lecturaHistorica = LecturaHistorica.from(payload);
        return lecturaHistoricaRepository.save(lecturaHistorica).getId();
    }

}
