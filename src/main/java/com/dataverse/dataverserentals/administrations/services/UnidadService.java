package com.dataverse.dataverserentals.administrations.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateUnidadDto;
import com.dataverse.dataverserentals.administrations.entities.Unidad;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
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

        if (unidadRepository.existsByEdificioIdAndNumeroPisoAndNumeroPuerta(
                payload.edificioId(), payload.numeroPiso(), payload.numeroPuerta())) {
            throw new ConflictException(String.format("Ya existe la puerta %s en el piso %s de este edificio.",
                    payload.numeroPuerta(), payload.numeroPiso()));
        }

        Unidad unidad = Unidad.from(payload);
        return unidadRepository.save(unidad).getId();

    }

}
