package com.dataverse.dataverserentals.administrations.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateEdificioDto;
import com.dataverse.dataverserentals.administrations.entities.Edificio;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.repositories.EdificioRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class EdificioService {

    private final EdificioRepository edificioRepository;

    public Page<Edificio> findAll(Pageable pageable, String q) {
        return edificioRepository.findByNombreContaining(pageable, q);
    }

    public Long create(@Valid CreateEdificioDto payload) throws ConflictException {

        log.info("Proceso de creación del edificio iniciado con payload: {}", payload);

        if (edificioRepository.existsByNombre(payload.nombre())) {
            throw new ConflictException("Ya existe un edificio con el nombre: " + payload.nombre());
        }

        Edificio edificio = Edificio.from(payload);
        return edificioRepository.save(edificio).getId();
    }

}
