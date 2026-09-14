package com.dataverse.dataverserentals.gestionadministrativa.controllers;

import java.util.Optional;

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
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailPersonaDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.PersonaPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;
import com.dataverse.dataverserentals.gestionadministrativa.services.PersonaService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/personas")
@Slf4j
public class PersonaController {

    private final PersonaService personaService;
    private final SecurityContext securityContext;

    @GetMapping
    public ResponseEntity<Page<PersonaPaginaDto>> findAll(
            Pageable pageable,
            @RequestParam(required = false) String q,
            @RequestParam Optional<TipoPersona> tipoPersonaCode) {

        log.info("Listando personas con filtro '{}', tipo: {} y parámetros: {}", q, tipoPersonaCode, pageable);
        org.springframework.data.domain.Page<PersonaPaginaDto> personaPage = personaService
                .findAll(pageable, q, tipoPersonaCode)
                .map(PersonaPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(personaPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreatePersonDto request)
            throws Exception {

        log.info("Iniciando la creación de una nueva persona: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = personaService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{personaId}")
    public ResponseEntity<DetailPersonaDto> findById(@PathVariable Long personaId) throws RecordNotFoundException {

        log.info("Consultando detalles de la persona con ID: {}", personaId);
        Persona persona = personaService.findById(personaId);
        DetailPersonaDto detailPersonaDto = DetailPersonaDto.fromEntity(persona);
        return new ResponseEntity<>(detailPersonaDto, HttpStatus.OK);
    }

    @PutMapping("{personaId}")
    public ResponseEntity<Void> update(@PathVariable Long personaId, @RequestBody UpdatePersonDto request)
            throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización de persona: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        personaService.update(personaId, request, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
