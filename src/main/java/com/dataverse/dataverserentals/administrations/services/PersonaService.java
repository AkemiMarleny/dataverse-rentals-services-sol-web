package com.dataverse.dataverserentals.administrations.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreatePersonaDto;
import com.dataverse.dataverserentals.administrations.entities.Persona;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.administrations.repositories.PersonaRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PersonaService {

    private final PersonaRepository personaRepository;

    public Long create(@Valid CreatePersonaDto payload) throws ConflictException, FieldConstraintException {

        log.info("Proceso de creación de la persona iniciado con la solicitud: {}", payload);

        Persona persona = Persona.from(payload);

        return personaRepository.save(persona).getId();
    }

}
