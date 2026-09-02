package com.dataverse.dataverserentals.gestionoperativa.services;

import org.springframework.stereotype.Service;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.gestionoperativa.commands.CalcularPropuestasCommand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropuestaLiquidacionService {
    private final CalcularPropuestasCommand calcularPropuestasCommand;

    public void calcularPropuestas() throws AccessDeniedException {
        calcularPropuestasCommand.execute();
    }
}
