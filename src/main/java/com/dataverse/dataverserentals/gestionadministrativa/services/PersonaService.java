package com.dataverse.dataverserentals.gestionadministrativa.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.validators.DocumentValidatorFactory;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.PersonaRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications.PersonaSpecifications;
import com.dataverse.dataverserentals.gestionadministrativa.validators.PersonaChecksumValidator;
import com.dataverse.dataverserentals.gestionadministrativa.validators.PersonaValidator;

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
    private final PersonaChecksumValidator personaChecksumValidator;
    private final PersonaValidator personaValidator;

    public Page<Persona> findAll(Pageable pageable, String q, Optional<TipoPersona> tipoPersonaCode) {

        log.info("Búsqueda paginada de personas [filtro: '{}', tipo: {}, pageable: {}]", q, tipoPersonaCode, pageable);

        return personaRepository.findAll(
                PersonaSpecifications.byTipoPersona(q, tipoPersonaCode), pageable);
    }

    public Long create(@Valid CreatePersonDto payload, Long usuarioId)
            throws ConflictException, FieldConstraintException {

        log.info("Registrando nueva persona con los siguientes datos: {}", payload);

        personaValidator.validarConsistenciaDatos(payload);

        documentValidatorFactory.getValidator(payload.tipoDocumentoCode())
                .validate(payload.numeroDocumento());

        Persona persona = Persona.from(payload, usuarioId);

        if (personaChecksumValidator.existsByChecksum(persona)) {
            log.info("Intento de registro duplicado: Checksum ya existe. Payload: {}", payload);
            throw new ConflictException("Ya existe otra persona registrada con los mismos datos.");
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

        Optional.ofNullable(persona.getNombreComercial())
                .filter(nombreComercial -> payload.tipoPersonaCode() == TipoPersona.NATURAL_CON_NEGOCIO)
                .filter(nombreComercial -> personaRepository.existsByNombreComercial(nombreComercial))
                .ifPresent(nombreComercial -> {
                    throw new ConflictException(
                            String.format("El nombre comercial %s ya está registrado.", nombreComercial));
                });

        Optional.ofNullable(persona.getRazonSocial())
                .filter(razonSocial -> payload.tipoPersonaCode() == TipoPersona.JURIDICA)
                .filter(razonSocial -> personaRepository.existsByRazonSocial(razonSocial))
                .ifPresent(razonSocial -> {
                    throw new ConflictException(
                            String.format("La razón social %s ya está registrada.", razonSocial));
                });

        return personaRepository.save(persona).getId();
    }

    public Persona findById(Long personaId) throws RecordNotFoundException {

        log.info("Recuperando datos de la persona con identificador: {}", personaId);
        return personaRepository.findById(personaId).orElseThrow(() -> new RecordNotFoundException(
                String.format("La persona con el ID %s no existe en nuestros registros.", personaId)));
    }

    public void update(Long personaId, @Valid UpdatePersonDto payload, Long usuarioId)
            throws RecordNotFoundException, ConflictException, FieldConstraintException {

        log.info("Iniciando actualización de la persona con ID: {}. Datos recibidos: {}", personaId, payload);

        Persona persona = findById(personaId);

        personaValidator.validarConsistenciaDatos(payload);

        documentValidatorFactory.getValidator(payload.tipoDocumentoCode())
                .validate(payload.numeroDocumento());

        persona.updateWith(payload, usuarioId);

        if (personaChecksumValidator.countByChecksum(persona) > 0) {
            log.info("Intento de registro duplicado: Checksum ya existe. Payload: {}", payload);
            throw new ConflictException("Ya existe otra persona registrada con los mismos datos (checksum duplicado).");
        }

        Long cantidadDocumentosDuplicados = personaRepository.countByTipoDocumentoCodeAndNumeroDocumentoAndIdNot(
                payload.tipoDocumentoCode(), payload.numeroDocumento(), personaId);
        if (cantidadDocumentosDuplicados > 0) {
            log.info("Validación fallida: Documento duplicado detectado para Tipo: {} y Número: {}",
                    payload.tipoDocumentoCode(), payload.numeroDocumento());
            throw new ConflictException(
                    String.format("El documento de identidad %s: %s ya se encuentra vinculado a otra persona.",
                            payload.tipoDocumentoCode(), payload.numeroDocumento()));
        }

        Long cantidadCelularesDuplicados = personaRepository.countByNumeroCelularAndIdNot(payload.numeroCelular(),
                personaId);
        if (cantidadCelularesDuplicados > 0) {
            log.info("Validación fallida: El número celular [{}] ya está vinculado a otra persona.",
                    payload.numeroCelular());
            throw new ConflictException(
                    String.format("El número de celular %s ya está registrado.", payload.numeroCelular()));
        }

        Long cantidadCorreosDuplicados = personaRepository.countByCorreoAndIdNot(payload.correo(), personaId);
        if (cantidadCorreosDuplicados > 0) {
            log.info("Validación fallida: Correo {} ya está en uso.", payload.correo());
            throw new ConflictException(
                    String.format("El correo %s ya se encuentra registrado en el sistema.", payload.correo()));
        }

        Optional.ofNullable(persona.getNombreComercial())
                .filter(nombreComercial -> payload.tipoPersonaCode() == TipoPersona.NATURAL_CON_NEGOCIO)
                .filter(nombreComercial -> personaRepository.countByNombreComercialAndIdNot(nombreComercial,
                        personaId) > 0)
                .ifPresent(nombreComercial -> {
                    throw new ConflictException(
                            String.format("El nombre comercial %s ya está registrado.", nombreComercial));
                });

        Optional.ofNullable(persona.getRazonSocial())
                .filter(razonSocial -> payload.tipoPersonaCode() == TipoPersona.JURIDICA)
                .filter(razonSocial -> personaRepository.countByRazonSocialAndIdNot(razonSocial, personaId) > 0)
                .ifPresent(razonSocial -> {
                    throw new ConflictException(
                            String.format("La razón social %s ya está registrada.", razonSocial));
                });

        personaRepository.save(persona);
    }
}