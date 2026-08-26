package com.dataverse.dataverserentals.administrations.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.request.UpdatePersonDto;
import com.dataverse.dataverserentals.administrations.entities.Persona;
import com.dataverse.dataverserentals.administrations.repositories.PersonaRepository;
import com.dataverse.dataverserentals.administrations.repositories.specifications.PersonaSpecifications;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PersonaService {

    private final PersonaRepository personaRepository;

    public Page<Persona> findAll(Pageable pageable, String q) {

        log.info("Búsqueda paginada de personas [filtro: '{}', pageable: {}]", q, pageable);
        return personaRepository.findAll(PersonaSpecifications.nombresOrApellidosOrNumeroDocumentoContains(q),
                pageable);
    }

    public Long create(@Valid CreatePersonDto payload) throws ConflictException, FieldConstraintException {

        log.info("Registrando nueva persona con los siguientes datos: {}", payload);
        Persona persona = Persona.from(payload);
        Long personaRepetida = personaRepository.countByChecksum(persona.getChecksum());
        if (personaRepetida > 0) {
            log.info("Esta persona ya fue registrada previamente.", payload);
            throw new ConflictException("Esta persona ya fue registrada previamente.");
        }

        if (personaRepository.existsByTipoDocumentoCodeAndNumeroDocumento(payload.tipoDocumentoCode(),
                payload.numeroDocumento())) {
            throw new ConflictException(
                    String.format("El documento de identidad %s: %s ya se encuentra vinculado a otra persona.",
                            payload.tipoDocumentoCode(), payload.numeroDocumento()));
        }
        return personaRepository.save(persona).getId();
    }

    public Persona findById(Long id) throws RecordNotFoundException {

        log.info("Recuperando datos de la persona con identificador: {}", id);
        return personaRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(
                String.format("La persona con el ID %s no existe en nuestros registros.", id)));
    }

    public void update(Long id, @Valid UpdatePersonDto payload) throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización de la persona con ID: {}. Datos recibidos: {}", id, payload);
        Persona persona = findById(id);
        Long cantidadDocumentosDuplicados = personaRepository.countByTipoDocumentoCodeAndNumeroDocumentoAndIdNot(
                payload.tipoDocumentoCode(), payload.numeroDocumento(), id);
        if (cantidadDocumentosDuplicados > 0) {
            log.info("Validación fallida: Documento duplicado detectado para Tipo: {} y Número: {}. Payload: {}",
                    payload);
            throw new ConflictException(
                    String.format("El documento de identidad %s: %s ya se encuentra vinculado a otra persona.",
                            payload.tipoDocumentoCode(), payload.numeroDocumento()));
        }
        persona.updateWith(payload);
        personaRepository.save(persona);
    }
}
