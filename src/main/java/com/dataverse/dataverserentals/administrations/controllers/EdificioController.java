package com.dataverse.dataverserentals.administrations.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateEdificioDto;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.services.EdificioService;
import com.dataverse.dataverserentals.core.dto.CreateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/buildings")
@Slf4j
public class EdificioController {

    private final EdificioService edificioService;

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateEdificioDto request) throws ConflictException {

        log.info("Creando un nuevo edificio con request: {}", request);

        Long id = edificioService.create(request);

        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

}
