package com.dataverse.dataverserentals.gestionoperativa.commands;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.enums.EstadoPropuesta;
import com.dataverse.dataverserentals.gestionoperativa.repositories.PropuestaLiquidacionRepository;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AprobarPropuestaLiquidacionCommand {
    private final PropuestaLiquidacionRepository propuestaLiquidacionRepository;

    @Transactional(rollbackFor = Exception.class)
    public void execute(Long propuestaLiquidacionId, UsuarioPrincipal usuarioPrincipal)
            throws RecordNotFoundException, UnprocesableEntity, AccessDeniedException {
        if (!RolUsuario.ADMINISTRADOR.equals(usuarioPrincipal.usuarioRol())) {
            throw new AccessDeniedException(usuarioPrincipal);
        }

        log.info("Aprobando propuesta de liquidacion con id: {}", propuestaLiquidacionId);

        PropuestaLiquidacion propuesta = propuestaLiquidacionRepository
                .findById(propuestaLiquidacionId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Propuesta de liquidacion no encontrada con id: " + propuestaLiquidacionId));

        if (propuesta.getEstadoCode() != EstadoPropuesta.PENDIENTE) {
            throw new UnprocesableEntity(
                    "La propuesta de liquidacion no permite cambiar el estado. Estado actual: "
                            + propuesta.getEstadoCode().name());
        }

        propuesta.aprobar(usuarioPrincipal);

        propuestaLiquidacionRepository.save(propuesta);

        log.info("Propuesta de liquidacion aprobada con exito. propuestaId: {}, aprobadorId: {}",
                propuestaLiquidacionId, usuarioPrincipal.usuarioId());
    }
}