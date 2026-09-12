package com.dataverse.dataverserentals.gestionadministrativa.services;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateContratoDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Contrato;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.ContratoRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.PersonaRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UnidadRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final UnidadRepository unidadRepository;
    private final PersonaRepository personaRepository;

    public Page<Contrato> findAll(Pageable pageable, String q) {
        log.info("Búsqueda paginada de contratos [filtro: '{}', pageable: {}]", q, pageable);
        if (StringUtils.hasText(q)) {
            return contratoRepository.findByPersonaNombresOrPersonaApellidosContaining(q, pageable);
        }
        return contratoRepository.findAll(pageable);
    }

    public Contrato findById(Long contratoId) throws RecordNotFoundException {
        log.info("Recuperando datos del contrato con identificador: {}", contratoId);
        return contratoRepository.findById(contratoId).orElseThrow(() -> new RecordNotFoundException(
                String.format("El contrato solicitado con el id: %s no se encuentra registrado.", contratoId)));
    }

    public Long create(@Valid CreateContratoDto payload, Long usuarioCreacionId)
            throws RecordNotFoundException, UnprocesableEntity {

        log.info("Registrando nuevo contrato con los siguientes datos: {}", payload);

        if (!unidadRepository.existsById(payload.unidadId())) {
            throw new FieldConstraintException(
                    "unidadId",
                    String.format("La unidad con el ID %s no existe.", payload.unidadId()));
        }

        if (!personaRepository.existsById(payload.personaId())) {
            throw new FieldConstraintException(
                    "personaId",
                    String.format("La persona con el ID %s no existe.", payload.personaId()));
        }

        if (payload.fechaInicio().isAfter(payload.fechaFin()) || payload.fechaInicio().isEqual(payload.fechaFin())) {
            throw new UnprocesableEntity("La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        Contrato contrato = Contrato.builder()
                .unidadId(payload.unidadId())
                .personaId(payload.personaId())
                .fechaInicio(payload.fechaInicio().atStartOfDay())
                .fechaFin(payload.fechaFin().atTime(LocalTime.MAX))
                .fechaCierre(null)
                .alquilerMensual(payload.alquilerMensual())
                .fechaCreacion(LocalDateTime.now())
                .usuarioCreacionId(usuarioCreacionId)
                .build();

        return contratoRepository.save(contrato).getId();
    }
}
