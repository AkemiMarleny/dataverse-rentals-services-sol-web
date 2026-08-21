package com.dataverse.dataverserentals.administrations.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreatePersonaDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.request.UpdatePersonDto;
import com.dataverse.dataverserentals.administrations.entities.Persona;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.administrations.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.administrations.repositories.PersonaRepository;
import com.dataverse.dataverserentals.administrations.repositories.specifications.PersonaSpecifications;

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
        return personaRepository.findAll(PersonaSpecifications.nombresOrApellidosOrNumeroDocumentoContains(q),
                pageable);
    }

    public Long create(@Valid CreatePersonaDto payload) throws ConflictException, FieldConstraintException {

        log.info("Proceso de creación de la persona iniciado con la solicitud: {}", payload);

        Persona persona = Persona.from(payload);

        return personaRepository.save(persona).getId();
    }

    public Persona findById(Long id) throws RecordNotFoundException {
        return personaRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(
                String.format("La persona solicitada con el id: %s no se encuentra registrada.", id)));
    }

    public void update(Long id, @Valid UpdatePersonDto payload) throws RecordNotFoundException {

        Persona persona = findById(id);
        persona.updateWith(payload);
        personaRepository.save(persona);
    }
}
