package com.dataverse.dataverserentals.administrations.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.request.UpdatePersonDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.response.DetailPersonaDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.response.PersonaPaginaDto;
import com.dataverse.dataverserentals.administrations.entities.Persona;
import com.dataverse.dataverserentals.administrations.services.PersonaService;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.core.dto.CreateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/people")
@Slf4j
public class PersonaController {

    private final PersonaService personaService;

    @GetMapping
    public ResponseEntity<Page<PersonaPaginaDto>> findAll(Pageable pageable, @RequestParam String q) {

        log.info("Listando personas con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<PersonaPaginaDto> personaPage = personaService.findAll(pageable, q)
                .map(PersonaPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(personaPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreatePersonDto request)
            throws Exception {

        log.info("Iniciando la creación de una nueva persona: {}", request);
        Long id = personaService.create(request);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<DetailPersonaDto> findById(@PathVariable Long id) throws RecordNotFoundException {

        log.info("Consultando detalles de la persona con ID: {}", id);
        Persona persona = personaService.findById(id);
        DetailPersonaDto detailPersonaDto = DetailPersonaDto.fromEntity(persona);
        return new ResponseEntity<>(detailPersonaDto, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UpdatePersonDto request)
            throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización de persona: {}", request);
        personaService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
