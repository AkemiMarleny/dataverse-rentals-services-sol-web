package com.dataverse.dataverserentals.gestionadministrativa.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.common.storage.FileStorageService;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoContrato;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateContratoDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Contrato;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.entities.VContrato;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.ContratoRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.PersonaRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UnidadRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.VContratoRepository;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;
import com.dataverse.dataverserentals.notifications.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final VContratoRepository vContratoRepository;
    private final UnidadRepository unidadRepository;
    private final PersonaRepository personaRepository;
    private final NotificationService notificationService;
    private final FileStorageService fileStorageService;
    private final SecurityContext securityContext;

    public Page<VContrato> findAll(
            Pageable pageable, String q, Optional<EstadoContrato> estadoContrato) throws AccessDeniedException {
        log.info("Búsqueda paginada de contratos [filtro: '{}', pageable: {}]", q, pageable);

        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();
        RolUsuario rol = usuarioPrincipal.usuarioRol();

        if (RolUsuario.ADMINISTRADOR.equals(rol)) {
            return vContratoRepository.findByEstadoAndPersonaNombresOrPersonaApellidosContaining(
                    estadoContrato.orElse(EstadoContrato.VIGENTE), q, pageable);
        }

        if (RolUsuario.INQUILINO.equals(rol)) {
            Long inquilinoId = usuarioPrincipal.personaId();
            return vContratoRepository.findByPersonaIdAndEstado(inquilinoId,
                    estadoContrato.orElse(EstadoContrato.VIGENTE), pageable);
        }

        throw new AccessDeniedException(usuarioPrincipal);
    }

    public Contrato findById(Long contratoId) throws AccessDeniedException, RecordNotFoundException {
        log.info("Recuperando datos del contrato con identificador: {}", contratoId);

        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();
        RolUsuario rol = usuarioPrincipal.usuarioRol();

        Contrato contrato = contratoRepository.findById(contratoId).orElseThrow(() -> new RecordNotFoundException(
                String.format("El contrato solicitado con el id: %s no se encuentra registrado.", contratoId)));

        if (RolUsuario.ADMINISTRADOR.equals(rol)) {
            return contrato;
        }

        if (RolUsuario.INQUILINO.equals(rol)) {
            if (!contrato.getPersonaId().equals(usuarioPrincipal.personaId())) {
                throw new AccessDeniedException(usuarioPrincipal);
            }
            return contrato;
        }

        throw new AccessDeniedException(usuarioPrincipal);
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

        Long contratoId = contratoRepository.save(contrato).getId();
        notificationService.sendEvent("CONTRATO_CREATED", contrato);

        return contratoId;
    }

    public void uploadContratto(
            Long contrattoId,
            MultipartFile file,
            UsuarioPrincipal usuarioPrincipal) throws AccessDeniedException, RecordNotFoundException {
        RolUsuario rol = usuarioPrincipal.usuarioRol();
        if (!RolUsuario.ADMINISTRADOR.equals(rol)) {
            throw new AccessDeniedException(usuarioPrincipal);
        }

        Contrato contrato = contratoRepository.findById(contrattoId).orElseThrow(() -> new RecordNotFoundException(
                String.format("El contrato solicitado con el id: %s no se encuentra registrado.", contrattoId)));

        String urlContratto = fileStorageService.uploadFile(file, "contratos/" + contrattoId);
        contrato.setPdfUrl(urlContratto);

        contratoRepository.save(contrato);
    }
}
