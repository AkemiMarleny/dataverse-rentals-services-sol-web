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
import com.dataverse.dataverserentals.common.validators.DocumentValidatorFactory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final DocumentValidatorFactory documentValidatorFactory;

    public Page<Persona> findAll(Pageable pageable, String q) {

        log.info("Búsqueda paginada de personas [filtro: '{}', pageable: {}]", q, pageable);
        return personaRepository.findAll(PersonaSpecifications.nombresOrApellidosOrNumeroDocumentoContains(q),
                pageable);
    }

    public Long create(@Valid CreatePersonDto payload) throws ConflictException, FieldConstraintException {

        log.info("Registrando nueva persona con los siguientes datos: {}", payload);

        documentValidatorFactory.getValidator(payload.tipoDocumentoCode())
                .validate(payload.numeroDocumento());

        Persona persona = Persona.from(payload);

        if (personaRepository.existsByChecksum(persona.getChecksum())) {
            log.info("Intento de registro duplicado: Persona con checksum {} ya existe. Payload: {}",
                    persona.getChecksum(), payload);
            throw new ConflictException("Ya existe otra persona registrada con los mismos datos (checksum duplicado).");
        }

        if (personaRepository.existsByTipoDocumentoCodeAndNumeroDocumento(payload.tipoDocumentoCode(),
                payload.numeroDocumento())) {
            throw new ConflictException(
                    String.format("El documento de identidad %s: %s ya se encuentra vinculado a otra persona.",
                            payload.tipoDocumentoCode(), payload.numeroDocumento()));
        }

        if (personaRepository.existsByNumeroCelular(payload.numeroCelular())) {
            throw new ConflictException(
                    String.format("El número de celular %s ya está registrado.", payload.numeroCelular()));
        }

        if (personaRepository.existsByCorreo(payload.correo())) {
            throw new ConflictException(
                    String.format("El correo %s ya se encuentra registrado en el sistema.", payload.correo()));
        }
        return personaRepository.save(persona).getId();
    }

    public Persona findById(Long id) throws RecordNotFoundException {

        log.info("Recuperando datos de la persona con identificador: {}", id);
        return personaRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(
                String.format("La persona con el ID %s no existe en nuestros registros.", id)));
    }

    public void update(Long id, @Valid UpdatePersonDto payload)
            throws RecordNotFoundException, ConflictException, FieldConstraintException {

        log.info("Iniciando actualización de la persona con ID: {}. Datos recibidos: {}", id, payload);

        documentValidatorFactory.getValidator(payload.tipoDocumentoCode())
                .validate(payload.numeroDocumento());

        Persona persona = findById(id);

        persona.updateWith(payload);

        Long personaRepetida = personaRepository.countByChecksumAndIdNot(persona.getChecksum(), id);
        if (personaRepetida > 0) {
            log.info("Intento de registro duplicado: Persona con checksum {} ya existe. Payload: {}",
                    persona.getChecksum(), payload);
            throw new ConflictException("Ya existe otra persona registrada con los mismos datos (checksum duplicado).");
        }

        Long cantidadDocumentosDuplicados = personaRepository.countByTipoDocumentoCodeAndNumeroDocumentoAndIdNot(
                payload.tipoDocumentoCode(), payload.numeroDocumento(), id);
        if (cantidadDocumentosDuplicados > 0) {
            log.info("Validación fallida: Documento duplicado detectado para Tipo: {} y Número: {}. Payload: {}",
                    payload);
            throw new ConflictException(
                    String.format("El documento de identidad %s: %s ya se encuentra vinculado a otra persona.",
                            payload.tipoDocumentoCode(), payload.numeroDocumento()));
        }

        Long cantidadCelularesDuplicados = personaRepository.countByNumeroCelularAndIdNot(payload.numeroCelular(), id);
        if (cantidadCelularesDuplicados > 0) {
            log.info(
                    "Validación fallida: El número celular [{}] ya está vinculado a otra persona (ID origen: {}).",
                    payload.numeroCelular(), id);
            throw new ConflictException(
                    String.format("El número de celular %s ya está registrado.", payload.numeroCelular()));
        }

        Long cantidadCorreosDuplicados = personaRepository.countByCorreoAndIdNot(payload.correo(), id);
        if (cantidadCorreosDuplicados > 0) {
            log.info("Validación fallida: Correo {} ya está en uso. Payload: {}", payload.correo(), payload);
            throw new ConflictException(
                    String.format("El correo %s ya se encuentra registrado en el sistema.", payload.correo()));
        }
        personaRepository.save(persona);
    }
}
