package com.dataverse.dataverserentals.gestionfinanciera.commands;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UnidadRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CalcularAreaArrendableEdificioCommand {

    private final UnidadRepository unidadRepository;

    public BigDecimal execute(Edificio edificio) {
        return unidadRepository.sumAreaMetrosCuadradosByEdificio(edificio.getId())
                .orElse(BigDecimal.ZERO);
    }
}
