package com.dataverse.dataverserentals.gestionfinanciera.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.common.storage.FileStorageService;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.ContratoRepository;
import com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.request.AuditarPagoDto;
import com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.request.CreatePagoDto;
import com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response.PagoAprobadoDto;
import com.dataverse.dataverserentals.gestionfinanciera.entities.Pago;
import com.dataverse.dataverserentals.gestionfinanciera.entities.Recibo;
import com.dataverse.dataverserentals.gestionfinanciera.enums.AccionAuditoria;
import com.dataverse.dataverserentals.gestionfinanciera.enums.EstadoPago;
import com.dataverse.dataverserentals.gestionfinanciera.repositories.PagoRepository;
import com.dataverse.dataverserentals.gestionfinanciera.repositories.ReciboRepository;
import com.dataverse.dataverserentals.notifications.NotificationService;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReciboRepository reciboRepository;
    private final ContratoRepository contratoRepository;
    private final NotificationService notificationService;
    private final FileStorageService fileStorageService;

    public Page<Pago> findPagosByReciboId(Long reciboId, UsuarioPrincipal usuarioPrincipal, Pageable pageable)
            throws AccessDeniedException {
        if (RolUsuario.ADMINISTRADOR.equals(usuarioPrincipal.usuarioRol())) {
            return pagoRepository.findAllByReciboId(reciboId, pageable);
        }

        if (RolUsuario.INQUILINO.equals(usuarioPrincipal.usuarioRol())) {
            Long personaId = usuarioPrincipal.personaId();
            List<Long> contratoIds = findContratoIdsByPersonaId(personaId);
            Recibo recibo = findReciboById(reciboId);

            if (!contratoIds.contains(recibo.getContratoId())) {
                throw new AccessDeniedException(usuarioPrincipal);
            }

            return pagoRepository.findAllByReciboId(reciboId, pageable);
        }

        throw new AccessDeniedException(usuarioPrincipal);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createPago(Long reciboId, CreatePagoDto request, MultipartFile file, UsuarioPrincipal usuarioPrincipal)
            throws AccessDeniedException {
        RolUsuario rol = usuarioPrincipal.usuarioRol();

        if (RolUsuario.ADMINISTRADOR.equals(rol)) {
            Recibo recibo = findReciboById(reciboId);
            return createPagoInternal(recibo, request, file, usuarioPrincipal.usuarioId());
        }

        if (RolUsuario.INQUILINO.equals(rol)) {
            Long personaId = usuarioPrincipal.personaId();
            List<Long> contratoIds = findContratoIdsByPersonaId(personaId);
            Recibo recibo = findReciboById(reciboId);

            if (!contratoIds.contains(recibo.getContratoId())) {
                throw new AccessDeniedException(usuarioPrincipal);
            }

            return createPagoInternal(recibo, request, file, usuarioPrincipal.usuarioId());
        }

        throw new AccessDeniedException(usuarioPrincipal);
    }

    private Long createPagoInternal(Recibo recibo, CreatePagoDto request, MultipartFile file, Long usuarioCreacionId) {
        Pago pago = Pago.builder()
                .reciboId(recibo.getId())
                .montoPagado(request.montoPagado())
                .observaciones(request.observaciones())
                .estadoCodigo(EstadoPago.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .usuarioCreacionId(usuarioCreacionId)
                .build();

        if (file != null && !file.isEmpty()) {
            String urlVoucher = fileStorageService.uploadFile(file, "pagos/" + recibo.getId());
            pago.setUrlVoucher(urlVoucher);
        }

        return pagoRepository.save(pago).getId();
    }

    public Recibo findReciboById(Long reciboId) {
        return reciboRepository.findById(reciboId)
                .orElseThrow(() -> new IllegalArgumentException("Recibo no encontrado con id: " + reciboId));
    }

    private Recibo findReciboByIdForUpdate(Long reciboId) {
        return reciboRepository.findByIdForUpdate(reciboId)
                .orElseThrow(() -> new IllegalArgumentException("Recibo no encontrado con id: " + reciboId));
    }

    public List<Long> findContratoIdsByPersonaId(Long personaId) {
        return contratoRepository.findContratoIdsByPersonaId(personaId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditarPago(Long reciboId, Long pagoId, AuditarPagoDto request, UsuarioPrincipal usuarioPrincipal)
            throws AccessDeniedException, UnprocesableEntity, FieldConstraintException {
        if (!RolUsuario.ADMINISTRADOR.equals(usuarioPrincipal.usuarioRol())) {
            throw new AccessDeniedException(usuarioPrincipal);
        }

        Pago pago = pagoRepository.findByIdForUpdate(pagoId)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado con id: " + pagoId));

        if (!pago.getReciboId().equals(reciboId)) {
            throw new IllegalArgumentException("El pago no pertenece al recibo especificado");
        }

        if (!EstadoPago.PENDIENTE.equals(pago.getEstadoCodigo())) {
            throw new IllegalStateException("Solo se pueden auditar pagos en estado PENDIENTE");
        }

        pago.setValidadorId(usuarioPrincipal.usuarioId());
        LocalDateTime now = LocalDateTime.now();

        if (AccionAuditoria.APROBAR.equals(request.accion())) {
            pago.setEstadoCodigo(EstadoPago.APROBADO);
            pago.setFechaAprobacion(now);

            Recibo recibo = findReciboByIdForUpdate(reciboId);
            recibo.agregarPago(pago);
            reciboRepository.save(recibo);
            notificationService.sendEvent("PAYMENT_APPROVED", new PagoAprobadoDto(reciboId));
        } else if (AccionAuditoria.RECHAZAR.equals(request.accion())) {
            if (request.motivoRechazo() == null || request.motivoRechazo().trim().isEmpty()) {
                throw new FieldConstraintException(
                        "motivo",
                        "El motivo de rechazo es requerido");
            }
            pago.setEstadoCodigo(EstadoPago.RECHAZADO);
            pago.setFechaRechazo(now);
            pago.setMotivoRechazo(request.motivoRechazo());
        }

        pagoRepository.save(pago);
    }
}
