package com.dataverse.dataverserentals.gestionadministrativa.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.CreateResponse;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestionadministrativa.services.LecturaHistoricaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/lecturas-historicas")
@Slf4j
public class LecturaHistoricaController {

    private final LecturaHistoricaService lecturaHistoricaService;

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateLecturaHistoricaDto request)
            throws FieldConstraintException {

        log.info("Creando una lectura histórica: medidorId={}, valor={}",
                request.medidorId(), request.valorLectura());

        Long lecturaHistoricaId = lecturaHistoricaService.create(request);
        return new ResponseEntity<>(new CreateResponse(lecturaHistoricaId), HttpStatus.CREATED);
    }

}
