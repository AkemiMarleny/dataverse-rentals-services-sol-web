package com.dataverse.dataverserentals.gestionoperativa.commands;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.EdificioRepository;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalcularPropuestasCommand {
    private final SecurityContext securityContext;
    private final EdificioRepository edificioRepository;
    private final CalcularPropuestaCommand calcularPropuestaCommand;

    @Transactional(rollbackFor = Exception.class)
    public void execute() throws AccessDeniedException {
        log.info("generando propuestas de liquidacion");

        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();
        if (!RolUsuario.SYSTEM.equals(usuarioPrincipal.usuarioRol())) {
            throw new AccessDeniedException(usuarioPrincipal);
        }

        List<Edificio> edificios = edificioRepository.findAll();
        edificios.stream().parallel().forEach(edificio -> {
            calcularPropuestaCommand.execute(edificio);
        });

    }
}
