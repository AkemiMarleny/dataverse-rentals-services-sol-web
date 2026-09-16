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
import com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications.UnidadSpecifications;

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

    public Page<Unidad> findAll(Pageable pageable, Long edificioId, String q) {

        log.info("Búsqueda paginada de unidades [filtro: '{}', pageable: {}]", q, pageable);
        return unidadRepository.findAll(UnidadSpecifications.byEdificioIdOrNumeroPuertaContains(edificioId, q),
                pageable);
    }

    public Long create(Long edificioId, @Valid CreateUnidadDto payload, Long usuarioId) throws ConflictException {

        log.info("Registrando nueva unidad con los siguientes datos: {}", payload);
        edificioRepository.findById(edificioId)
                .orElseThrow(() -> new FieldConstraintException("edificioId",
                        "El edificio solicitado con el id: %s no se encuentra registrado."));

        if (unidadRepository.existsByEdificioIdAndNumeroPuertaIgnoreCase(
                edificioId, payload.numeroPuerta())) {
            throw new ConflictException(String.format("La puerta %s ya se encuentra registrada en este edificio.",
                    payload.numeroPuerta()));
        }
        Unidad unidad = Unidad.from(edificioId, payload, usuarioId);
        return unidadRepository.save(unidad).getId();
    }

    public Unidad findById(Long edificioId, Long unidadId) throws RecordNotFoundException {

        log.info("Consultando detalles de la unidad con ID: {}", unidadId);
        return unidadRepository.findByEdificioIdAndId(edificioId, unidadId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("No se encontró la unidad con ID: %s para el edificio con ID: %s.", unidadId,
                                edificioId)));
    }

    public void update(Long edificioId, Long unidadId, @Valid UpdateUnidadDto payload, Long usuarioId)
            throws ConflictException, RecordNotFoundException {

        log.info("Iniciando actualización de la unidad con ID: {}. Datos recibidos: {}", unidadId, payload);

        edificioRepository.findById(edificioId)
                .orElseThrow(() -> new RecordNotFoundException(String.format(
                        "El edificio solicitado con el id: %s no se encuentra registrado.", edificioId)));

        Unidad unidad = findById(edificioId, unidadId);

        Long cantidadPuertasDuplicadas = unidadRepository.countByEdificioIdAndNumeroPuertaIgnoreCaseAndIdNot(edificioId,
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