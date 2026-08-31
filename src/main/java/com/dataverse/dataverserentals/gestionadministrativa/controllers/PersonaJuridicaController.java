package com.dataverse.dataverserentals.gestionadministrativa.controllers;

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

import com.dataverse.dataverserentals.common.dto.CreateResponse;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonaJuridicaDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdatePersonaJuridicaDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailPersonaJuridicaDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.PersonaJuridicaPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.PersonaJuridica;
import com.dataverse.dataverserentals.gestionadministrativa.services.PersonaJuridicaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/personas-juridicas")
@RequiredArgsConstructor
@Slf4j
public class PersonaJuridicaController {

    private final PersonaJuridicaService personaJuridicaService;

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreatePersonaJuridicaDto request)
            throws ConflictException {

        log.info("Iniciando la creación de una nueva persona jurídica: {}", request);
        Long personaJuridicaId = personaJuridicaService.create(request);
        return new ResponseEntity<>(new CreateResponse(personaJuridicaId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PersonaJuridicaPaginaDto>> findAll(Pageable pageable, @RequestParam String q) {
        log.info("Listando personas jurídicas con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<PersonaJuridicaPaginaDto> personaJuridicaPage = personaJuridicaService
                .findAll(pageable, q).map(PersonaJuridicaPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(personaJuridicaPage));
    }

    @GetMapping("{personaJuridicaId}")
    public ResponseEntity<DetailPersonaJuridicaDto> findById(@PathVariable Long personaJuridicaId)
            throws RecordNotFoundException {
        log.info("Consultando detalles de la persona jurídica con ID: {}", personaJuridicaId);
        PersonaJuridica personaJuridica = personaJuridicaService.findById(personaJuridicaId);
        DetailPersonaJuridicaDto detailPersonaJuridicaDto = DetailPersonaJuridicaDto.fromEntity(personaJuridica);
        return new ResponseEntity<>(detailPersonaJuridicaDto, HttpStatus.OK);
    }

    @PutMapping("{personaJuridicaId}")
    public ResponseEntity<Void> update(@PathVariable Long personaJuridicaId,
            @RequestBody UpdatePersonaJuridicaDto request) throws RecordNotFoundException, ConflictException {
        log.info("Iniciando actualización de la persona jurídica: {}", request);
        personaJuridicaService.update(personaJuridicaId, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}