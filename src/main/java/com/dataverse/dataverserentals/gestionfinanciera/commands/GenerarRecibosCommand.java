package com.dataverse.dataverserentals.gestionfinanciera.commands;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.EdificioRepository;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerarRecibosCommand {

    private final EdificioRepository edificioRepository;

    private final GenerarRecibosEdificioCommand generarRecibosEdificioCommand;

    @Transactional(rollbackFor = Exception.class)
    public void execute(UsuarioPrincipal usuarioPrincipal) throws AccessDeniedException {

        if (!RolUsuario.SYSTEM.equals(usuarioPrincipal.usuarioRol())
                && !RolUsuario.ADMINISTRADOR.equals(usuarioPrincipal.usuarioRol())) {
            log.error("usuario no autorizado para generar recibos");
            throw new AccessDeniedException(usuarioPrincipal);
        }

        List<Edificio> edificios = edificioRepository.findAll();
        edificios.stream().parallel().forEach(edificio -> {
            generarRecibosEdificioCommand.execute(edificio, usuarioPrincipal);
        });
    }
}
