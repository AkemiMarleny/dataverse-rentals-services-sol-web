package com.dataverse.dataverserentals.gestionoperativa.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionoperativa.commands.AprobarPropuestaLiquidacionCommand;
import com.dataverse.dataverserentals.gestionoperativa.commands.CalcularPropuestasCommand;
import com.dataverse.dataverserentals.gestionoperativa.commands.RecalcularPropuestaCommand;
import com.dataverse.dataverserentals.gestionoperativa.entities.DetallePropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.repositories.DetallePropuestaLiquidacionRepository;
import com.dataverse.dataverserentals.gestionoperativa.repositories.PropuestaLiquidacionRepository;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropuestaLiquidacionService {
    private final AprobarPropuestaLiquidacionCommand aprobarPropuestaLiquidacionCommand;
    private final CalcularPropuestasCommand calcularPropuestasCommand;
    private final RecalcularPropuestaCommand recalcularPropuestaCommand;
    private final PropuestaLiquidacionRepository propuestaLiquidacionRepository;
    private final DetallePropuestaLiquidacionRepository detallePropuestaLiquidacionRepository;
    private final SecurityContext securityContext;

    public void calcularPropuestas() throws AccessDeniedException {
        calcularPropuestasCommand.execute();
    }

    public void recalcularPropuesta(Long propuestaId)
            throws RecordNotFoundException, UnprocesableEntity, AccessDeniedException {
        if (!RolUsuario.ADMINISTRADOR.equals(securityContext.getUsuarioPrincipal().usuarioRol())) {
            throw new AccessDeniedException(securityContext.getUsuarioPrincipal());
        }
        recalcularPropuestaCommand.execute(propuestaId);
    }

    public Page<PropuestaLiquidacion> findAllPropuestasLiquidacion(
            Pageable pageable,
            Optional<Long> edificioId,
            Optional<LocalDate> fechaDesde,
            Optional<LocalDate> fechaHasta) throws AccessDeniedException {
        if (!RolUsuario.ADMINISTRADOR.equals(securityContext.getUsuarioPrincipal().usuarioRol())) {
            throw new AccessDeniedException(securityContext.getUsuarioPrincipal());
        }

        return propuestaLiquidacionRepository.findAllWithFilters(
                edificioId,
                fechaDesde.map(desde -> desde.atTime(LocalTime.MIN)),
                fechaHasta.map(hasta -> hasta.atTime(LocalTime.MAX)),
                pageable);
    }

    public PropuestaLiquidacion findPropuestaLiquidacionDetails(Long propuestaLiquidacionId)
            throws RecordNotFoundException, AccessDeniedException {
        if (!RolUsuario.ADMINISTRADOR.equals(securityContext.getUsuarioPrincipal().usuarioRol())) {
            throw new AccessDeniedException(securityContext.getUsuarioPrincipal());
        }
        return propuestaLiquidacionRepository.findById(propuestaLiquidacionId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Propuesta de liquidación no encontrada con id: " + propuestaLiquidacionId));
    }

    public Page<DetallePropuestaLiquidacion> findDetallesByPropuestaId(
            Long propuestaLiquidacionId, Pageable pageable) throws AccessDeniedException {
        if (!RolUsuario.ADMINISTRADOR.equals(securityContext.getUsuarioPrincipal().usuarioRol())) {
            throw new AccessDeniedException(securityContext.getUsuarioPrincipal());
        }
        return detallePropuestaLiquidacionRepository.findByPropuestaLiquidacionId(
                propuestaLiquidacionId, pageable);
    }

    public void aprobarPropuestaLiquidacion(
            Long propuestaLiquidacionId, UsuarioPrincipal usuarioPrincipal)
            throws RecordNotFoundException, UnprocesableEntity, AccessDeniedException {
        aprobarPropuestaLiquidacionCommand.execute(propuestaLiquidacionId, usuarioPrincipal);
    }
}
