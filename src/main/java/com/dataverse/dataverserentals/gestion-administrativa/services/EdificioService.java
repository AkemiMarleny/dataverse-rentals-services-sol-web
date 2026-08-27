package com.dataverse.dataverserentals.administrations.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateEdificioDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.request.UpdateEdificioDto;
import com.dataverse.dataverserentals.administrations.entities.Edificio;
import com.dataverse.dataverserentals.administrations.repositories.EdificioRepository;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;

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
        log.info("Búsqueda paginada de edificios [filtro: '{}', pageable: {}]", q, pageable);
        return edificioRepository.findByNombreContaining(pageable, q);
    }

    public Long create(@Valid CreateEdificioDto payload) throws ConflictException {
        log.info("Registrando nuevo edificio con los siguientes datos: {}", payload);
        if (edificioRepository.existsByNombre(payload.nombre())) {
            throw new ConflictException(String.format("Ya existe un edificio con el nombre: %s", payload.nombre()));
        }
        Edificio edificio = Edificio.from(payload);
        return edificioRepository.save(edificio).getId();
    }

    public Edificio findById(Long id) throws RecordNotFoundException {
        log.info("Recuperando datos del edificio con identificador: {}", id);
        return edificioRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(
                String.format("El edificio solicitado con el id: %s no se encuentra registrado.", id)));
    }

    public void update(Long id, @Valid UpdateEdificioDto payload) throws RecordNotFoundException, ConflictException {
        log.info("Iniciando actualización del edificio con ID: {}. Datos recibidos: {}", id, payload);
        Edificio edificio = findById(id);

        Long cantidadEdificiosDuplicados = edificioRepository.countByNombreAndIdNot(payload.nombre(), id);
        if (cantidadEdificiosDuplicados > 0) {
            log.info("Validación fallida: Ya existe otro edificio con el mismo nombre: {}", payload.nombre());
            throw new ConflictException(
                    String.format("No se puede actualizar: ya existe este nombre registrado: %s",
                            payload.nombre()));
        }

        edificio.updateWith(payload);
        edificioRepository.save(edificio);
    }

}
