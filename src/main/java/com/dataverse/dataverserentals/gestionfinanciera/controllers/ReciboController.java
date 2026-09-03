package com.dataverse.dataverserentals.gestionfinanciera.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.gestionfinanciera.services.ReciboService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recibos")
public class ReciboController {

    private final ReciboService reciboService;

    @PostMapping
    ResponseEntity<Void> generarRecibos() throws AccessDeniedException {
        reciboService.generarRecibos();

        return ResponseEntity.noContent().build();
    }
}
