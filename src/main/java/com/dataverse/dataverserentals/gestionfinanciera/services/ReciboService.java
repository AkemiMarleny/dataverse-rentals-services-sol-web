package com.dataverse.dataverserentals.gestionfinanciera.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.ContratoRepository;
import com.dataverse.dataverserentals.gestionfinanciera.commands.GenerarRecibosCommand;
import com.dataverse.dataverserentals.gestionfinanciera.entities.Recibo;
import com.dataverse.dataverserentals.gestionfinanciera.enums.EstadoRecibo;
import com.dataverse.dataverserentals.gestionfinanciera.repositories.ReciboRepository;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class ReciboService {

    private final GenerarRecibosCommand generarRecibosCommand;
    private final SecurityContext securityContext;
    private final ReciboRepository reciboRepository;
    private final ContratoRepository contratoRepository;

    public void generarRecibos() throws AccessDeniedException {
        generarRecibosCommand.execute(securityContext.getUsuarioPrincipal());
    }

    public Page<Recibo> findAllRecibos(
            Pageable pageable,
            Optional<LocalDate> fechaDesde,
            Optional<LocalDate> fechaHasta,
            Optional<EstadoRecibo> estadoCode) throws AccessDeniedException {
        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();
        RolUsuario rol = usuarioPrincipal.usuarioRol();

        if (RolUsuario.ADMINISTRADOR.equals(rol)) {
            return reciboRepository.findAllRecibos(
                    estadoCode,
                    fechaDesde.map(desde -> desde.atTime(LocalTime.MIN)),
                    fechaHasta.map(hasta -> hasta.atTime(LocalTime.MAX)),
                    pageable);
        }

        if (RolUsuario.INQUILINO.equals(rol)) {
            Long inquilinoId = usuarioPrincipal.personaId();
            List<Long> contratoIds = contratoRepository.findContratoIdsByPersonaId(inquilinoId);

            return reciboRepository.findAllInquilinoRecibos(
                    estadoCode,
                    fechaDesde.map(desde -> desde.atTime(LocalTime.MIN)),
                    fechaHasta.map(hasta -> hasta.atTime(LocalTime.MAX)),
                    contratoIds,
                    pageable);
        }

        throw new AccessDeniedException(usuarioPrincipal);
    }
}