package com.dataverse.dataverserentals.gestionadministrativa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateMedidorDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateMedidorDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.MedidorRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UnidadRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications.MedidorSpecifications;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class MedidorService {

    private final MedidorRepository medidorRepository;
    private final UnidadRepository unidadRepository;

    public Page<Medidor> findAll(Pageable pageable, String q) {

        log.info("Búsqueda paginada de medidores [filtro: '{}', pageable: {}]", q, pageable);
        return medidorRepository.findAll(MedidorSpecifications.tipoMedidorOrNumeroSerieContains(q), pageable);
    }

    public Long create(@Valid CreateMedidorDto payload, Long unidadId)
            throws ConflictException, RecordNotFoundException {

        log.info("Registrando nuevo medidor con los siguientes datos: {}", payload);
        unidadRepository.findById(unidadId).orElseThrow(() -> new RecordNotFoundException(
                String.format("La unidad solicitada con el id: %s no se encuentra registrada.", "unidadId")));

        if (medidorRepository.existsByUnidadIdAndTipoMedidorCode(unidadId, payload.tipoMedidorCode())) {
            throw new ConflictException(String.format(
                    "Ya existe un medidor de tipo %s para la unidad %s",
                    payload.tipoMedidorCode(), unidadId));
        }

        if (medidorRepository.existsByNumeroSerie(payload.numeroSerie())) {
            throw new ConflictException(String.format(
                    "El número de serie %s ya está registrada en el sistema",
                    payload.numeroSerie()));
        }
        Medidor medidor = Medidor.from(payload, unidadId);
        return medidorRepository.save(medidor).getId();
    }

    public Medidor findById(Long medidorId) throws RecordNotFoundException {

        log.info("Recuperando datos del medidor con identificador: {}", medidorId);
        return medidorRepository.findById(medidorId).orElseThrow(() -> new RecordNotFoundException(
                String.format("El medidor con el ID %s no existe en nuestros registros.", medidorId)));
    }

    public void update(@Valid UpdateMedidorDto payload, Long unidadId, Long medidorId)
            throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización del medidor con ID: {} para la unidad: {}. Datos: {}", medidorId, unidadId,
                payload);
        Medidor medidor = medidorRepository.findByUnidadIdAndId(unidadId, medidorId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("No se encontró el medidor con ID %s asociado a la unidad con ID %s", medidorId,
                                unidadId)));

        Long cantidadMedidoresDuplicados = medidorRepository.countByUnidadIdAndTipoMedidorCodeAndIdNot(unidadId,
                payload.tipoMedidorCode(), medidorId);

        if (cantidadMedidoresDuplicados > 0) {
            log.info(
                    "Validación fallida: Ya existe otro medidor de tipo {} en la unidad {}. (Intentando actualizar medidor ID: {})",
                    payload.tipoMedidorCode(), unidadId, medidorId);
            throw new ConflictException(String.format("La unidad con ID %s ya tiene registrado un medidor de tipo %s",
                    unidadId, payload.tipoMedidorCode()));
        }

        Long seriesDuplicadas = medidorRepository.countByNumeroSerieAndIdNot(payload.numeroSerie(),
                medidorId);

        if (seriesDuplicadas > 0) {
            log.info("Conflicto de actualización: El número de serie {} ya está en uso por otro medidor ID: {}",
                    payload.numeroSerie(), medidorId);
            throw new ConflictException(
                    String.format("No se puede actualizar, ya existe un medidor registrado con el número de serie '%s'",
                            payload.numeroSerie()));
        }

        medidor.updateWith(payload);
        medidorRepository.save(medidor);
    }

}
