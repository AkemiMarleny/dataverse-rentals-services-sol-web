package com.dataverse.dataverserentals.gestionadministrativa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateUnidadDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateUnidadDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.EdificioRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UnidadRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class UnidadService {

    private final UnidadRepository unidadRepository;
    private final EdificioRepository edificioRepository;

    public Page<Unidad> findAll(Pageable pageable, String q) {

        log.info("Búsqueda paginada de unidades [filtro: '{}', pageable: {}]", q, pageable);
        return unidadRepository.nombreEdificioOrNumeroPisoOrNumeroPuertaContains(q, pageable);
    }

    public Long create(@Valid CreateUnidadDto payload, Long usuarioId) throws ConflictException {

        log.info("Registrando nueva unidad con los siguientes datos: {}", payload);
        edificioRepository.findById(payload.edificioId())
                .orElseThrow(() -> new FieldConstraintException("edificioId",
                        "El edificio solicitado con el id: %s no se encuentra registrado."));

        if (unidadRepository.existsByEdificioIdAndNumeroPuerta(
                payload.edificioId(), payload.numeroPuerta())) {
            throw new ConflictException(String.format("La puerta %s ya se encuentra registrada en este edificio.",
                    payload.numeroPuerta()));
        }
        Unidad unidad = Unidad.from(payload, usuarioId);
        return unidadRepository.save(unidad).getId();
    }

    public Unidad findById(Long unidadId) throws RecordNotFoundException {

        log.info("Recuperando datos de la unidad con identificador: {}", unidadId);
        return unidadRepository.findById(unidadId).orElseThrow(() -> new RecordNotFoundException(
                String.format("La unidad solicitada con el id: %s no se encuentra registrada.", unidadId)));
    }

    public void update(Long unidadId, @Valid UpdateUnidadDto payload, Long usuarioId)
            throws ConflictException, RecordNotFoundException {

        log.info("Iniciando actualización de la unidad con ID: {}. Datos recibidos: {}", unidadId, payload);
        Unidad unidad = findById(unidadId);

        edificioRepository.findById(payload.edificioId())
                .orElseThrow(() -> new FieldConstraintException("edificioId",
                        "El edificio solicitado con el id: %s no se encuentra registrado."));

        Long cantidadPuertasDuplicadas = unidadRepository.countByEdificioIdAndNumeroPuertaAndIdNot(payload.edificioId(),
                payload.numeroPuerta(), unidadId);

        if (cantidadPuertasDuplicadas > 0) {
            log.info("Intento de registro de puerta duplicada en actualización: {}", payload);
            throw new ConflictException(String.format("La puerta %s ya se encuentra registrada en este edificio.",
                    payload.numeroPuerta()));
        }
        unidad.updateWith(payload, usuarioId);
        unidadRepository.save(unidad);
    }
}
