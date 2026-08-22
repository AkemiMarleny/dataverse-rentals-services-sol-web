package com.dataverse.dataverserentals.administrations.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateUnidadDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.request.UpdateUnidadDto;
import com.dataverse.dataverserentals.administrations.entities.Unidad;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.administrations.repositories.EdificioRepository;
import com.dataverse.dataverserentals.administrations.repositories.UnidadRepository;

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
        return unidadRepository.nombreEdificioOrNumeroPisoOrNumeroPuertaContains(q, pageable);
    }

    public Long create(@Valid CreateUnidadDto payload) throws ConflictException {

        log.info("Proceso de creación de la unidad iniciado con payload: {}", payload);

        edificioRepository.findById(payload.edificioId()).orElseThrow(() -> new ConflictException(
                String.format("El edificio solicitado con el id: %s no se encuentra registrado.",
                        payload.edificioId())));

        if (unidadRepository.existsByEdificioIdAndNumeroPuerta(
                payload.edificioId(), payload.numeroPuerta())) {
            throw new ConflictException(String.format("La puerta %s ya se encuentra registrada en este edificio.",
                    payload.numeroPuerta()));
        }

        Unidad unidad = Unidad.from(payload);
        return unidadRepository.save(unidad).getId();

    }

    public Unidad findById(Long id) throws RecordNotFoundException {
        return unidadRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(
                String.format("La unidad solicitada con el id: %s no se encuentra registrada.", id)));
    }

    public void update(Long id, @Valid UpdateUnidadDto payload) throws ConflictException, RecordNotFoundException {
        Unidad unidad = findById(id);

        edificioRepository.findById(payload.edificioId())
                .orElseThrow(() -> new ConflictException(String.format(
                        "El edificio solicitado con el id: %s no se encuentra registrado.", payload.edificioId())));

        Long cantidadPuertasDuplicadas = unidadRepository.countByEdificioIdAndNumeroPuertaAndIdNot(payload.edificioId(),
                payload.numeroPuerta(), id);

        if (cantidadPuertasDuplicadas > 0) {
            log.info("Intento de registro de puerta duplicada en actualización: {}", payload);
            throw new ConflictException(String.format("La puerta %s ya se encuentra registrada en este edificio.",
                    payload.numeroPuerta()));
        }

        unidad.updateWith(payload);
        unidadRepository.save(unidad);

    }

}
