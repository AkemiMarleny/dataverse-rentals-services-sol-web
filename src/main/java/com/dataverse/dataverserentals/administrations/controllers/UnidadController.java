package com.dataverse.dataverserentals.administrations.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateUnidadDto;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.services.UnidadService;
import com.dataverse.dataverserentals.core.dto.CreateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/units")
@Slf4j
public class UnidadController {

    private final UnidadService unidadService;

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateUnidadDto request) throws ConflictException {

        log.info("Creando un nueva nueva con request: {}", request);
        Long id = unidadService.create(request);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

}
