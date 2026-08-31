package com.dataverse.dataverserentals.gestionadministrativa.services;

import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonaJuridicaDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdatePersonaJuridicaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.PersonaJuridica;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.PersonaJuridicaRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications.PersonaJuridicaSpecifications;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PersonaJuridicaService {

    private final PersonaJuridicaRepository personaJuridicaRepository;

    public Long create(@Valid CreatePersonaJuridicaDto payload) throws ConflictException {

        log.info("Registrando nueva persona jurídica con los siguientes datos: {}", payload);

        PersonaJuridica personaJuridica = PersonaJuridica.from(payload);

        Long sameChecksum = personaJuridicaRepository.countByChecksum(personaJuridica.getChecksum());
        if (sameChecksum > 0) {
            log.info("Esta persona jurídica ya fue registrada previamente: {}", payload);
            throw new ConflictException("Esta persona jurídica ya fue registrada previamente.");
        }

        Long hasSameDoc = personaJuridicaRepository.countByTipoDocumentoCodeAndNumeroDocumento(
                personaJuridica.getTipoDocumentoCode(), personaJuridica.getNumeroDocumento());

        if (hasSameDoc > 0) {
            log.info("El tipo y el número de documento ya han sido registrados anteriormente: {}", payload);
            throw new ConflictException("El tipo y el número de documento ya han sido registrados anteriormente.");
        }

        return personaJuridicaRepository.save(personaJuridica).getPersonaJuridicaId();
    }

    public Page<PersonaJuridica> findAll(Pageable pageable, String q) {
        log.info("Búsqueda paginada de personas jurídicas [filtro: '{}', pageable: {}]", q, pageable);
        return personaJuridicaRepository
                .findAll(PersonaJuridicaSpecifications.razonSocialOrNumeroDocumentoContains(q), pageable);
    }

    public PersonaJuridica findById(Long personaJuridicaId) throws RecordNotFoundException {
        log.info("Recuperando datos de la persona jurídica con identificador: {}", personaJuridicaId);
        return personaJuridicaRepository.findById(personaJuridicaId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("La persona jurídica solicitada con el id: %s no se encuentra registrada",
                                personaJuridicaId)));
    }

    public void update(Long personaJuridicaId, @Valid UpdatePersonaJuridicaDto payload)
            throws RecordNotFoundException, ConflictException {
        log.info("Iniciando actualización de la persona jurídica con ID: {}. Datos recibidos: {}", personaJuridicaId, payload);
        PersonaJuridica personaJuridica = personaJuridicaRepository.findById(personaJuridicaId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("La persona jurídica solicitada con el id: %s no se encuentra registrada",
                                personaJuridicaId)));

        personaJuridica.updateWith(payload);

        Long sameChecksum = personaJuridicaRepository.countByChecksumAndPersonaJuridicaIdNotIn(
                personaJuridica.getChecksum(), Arrays.asList(personaJuridicaId));
        if (sameChecksum > 0) {
            log.info("Esta razón social ya fue registrada previamente: {}", payload);
            throw new ConflictException("Esta razón social ya fue registrada previamente.");
        }

        Long hasSameDoc = personaJuridicaRepository.countByTipoDocumentoCodeAndNumeroDocumentoAndPersonaJuridicaIdNotIn(
                personaJuridica.getTipoDocumentoCode(), personaJuridica.getNumeroDocumento(),
                Arrays.asList(personaJuridicaId));

        if (hasSameDoc > 0) {
            log.info("El tipo y el número de documento ya han sido registrados anteriormente: {}", payload);
            throw new ConflictException("El tipo y el número de documento ya han sido registrados anteriormente.");
        }

        personaJuridicaRepository.save(personaJuridica);
    }
}