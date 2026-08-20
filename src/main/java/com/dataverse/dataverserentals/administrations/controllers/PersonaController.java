package com.dataverse.dataverserentals.administrations.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreatePersonaDto;
import com.dataverse.dataverserentals.administrations.services.PersonaService;
import com.dataverse.dataverserentals.core.dto.CreateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/people")
@Slf4j
public class PersonaController {

    private final PersonaService personaService;

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreatePersonaDto request)
            throws Exception {

        log.info("Creando una nueva persona con solicitud: {}", request);

        Long id = personaService.create(request);

        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

}
