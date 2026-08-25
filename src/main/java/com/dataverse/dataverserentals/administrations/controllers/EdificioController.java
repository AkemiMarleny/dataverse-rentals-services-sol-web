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

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateEdificioDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.request.UpdateEdificioDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.response.DetailEdificioDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.response.EdificioPaginaDto;
import com.dataverse.dataverserentals.administrations.entities.Edificio;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.administrations.services.EdificioService;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.core.dto.CreateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/buildings")
@Slf4j
public class EdificioController {

    private final EdificioService edificioService;

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
        Long id = edificioService.create(request);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<DetailEdificioDto> findById(@PathVariable Long id) throws RecordNotFoundException {
        log.info("Consultando detalles del edificio con ID: {}", id);
        Edificio edificio = edificioService.findById(id);
        DetailEdificioDto detailEdificioDto = DetailEdificioDto.fromEntity(edificio);
        return new ResponseEntity<>(detailEdificioDto, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UpdateEdificioDto request)
            throws RecordNotFoundException, ConflictException {
        log.info("Iniciando actualización del edificio: {}", request);
        edificioService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
