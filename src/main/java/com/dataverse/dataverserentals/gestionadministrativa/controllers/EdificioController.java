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
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateEdificioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateEdificioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailEdificioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.EdificioPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.services.EdificioService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/edificios")
@Slf4j
public class EdificioController {

    private final EdificioService edificioService;
    private final SecurityContext securityContext;

    @GetMapping
    public ResponseEntity<Page<EdificioPaginaDto>> findAll(Pageable pageable, @RequestParam String q) {

        log.info("Listando edificios con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<EdificioPaginaDto> edificioPage = edificioService.findAll(pageable, q)
                .map(EdificioPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(edificioPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateEdificioDto request) throws ConflictException {

        log.info("Iniciando la creación de una nuevo edificio: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = edificioService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{edificioId}")
    public ResponseEntity<DetailEdificioDto> findById(@PathVariable Long edificioId) throws RecordNotFoundException {

        log.info("Consultando detalles del edificio con ID: {}", edificioId);
        Edificio edificio = edificioService.findById(edificioId);
        DetailEdificioDto detailEdificioDto = DetailEdificioDto.fromEntity(edificio);
        return new ResponseEntity<>(detailEdificioDto, HttpStatus.OK);
    }

    @PutMapping("{edificioId}")
    public ResponseEntity<Void> update(@PathVariable Long edificioId, @RequestBody UpdateEdificioDto request)
            throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización del edificio: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        edificioService.update(edificioId, request, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
