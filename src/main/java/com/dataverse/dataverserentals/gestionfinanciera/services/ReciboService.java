package com.dataverse.dataverserentals.gestionfinanciera.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.gestionfinanciera.commands.GenerarRecibosCommand;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class ReciboService {

    private final GenerarRecibosCommand generarRecibosCommand;
    private final SecurityContext securityContext;

    public void generarRecibos() throws AccessDeniedException {
        generarRecibosCommand.execute(securityContext.getUsuarioPrincipal());
    }
}
