package com.dataverse.dataverserentals.gestionoperativa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.gestionoperativa.services.PropuestaLiquidacionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/propuestas")
@RestController
public class PropuestaLiquidacionController {

    private final PropuestaLiquidacionService propuestaLiquidacionService;

    @PostMapping()
    ResponseEntity<Void> generarPropuestas() throws AccessDeniedException {
        log.info("Iniciando calculo de propuestas de liquidación");
        propuestaLiquidacionService.calcularPropuestas();

        return ResponseEntity.noContent().build();
    }
}
