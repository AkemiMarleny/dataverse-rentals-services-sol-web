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
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateUnidadDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateUnidadDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailUnidadDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.UnidadPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;
import com.dataverse.dataverserentals.gestionadministrativa.services.UnidadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/units")
@Slf4j
public class UnidadController {

    private final UnidadService unidadService;

    @GetMapping
    public ResponseEntity<Page<UnidadPaginaDto>> findAll(Pageable pageable, @RequestParam String q) {
        org.springframework.data.domain.Page<UnidadPaginaDto> unidadPage = unidadService.findAll(pageable, q)
                .map(UnidadPaginaDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(unidadPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateUnidadDto request) throws ConflictException {

        log.info("Creando un nueva nueva con request: {}", request);
        Long id = unidadService.create(request);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<DetailUnidadDto> findById(@PathVariable Long id) throws RecordNotFoundException {
        Unidad unidad = unidadService.findById(id);
        DetailUnidadDto detailUnidadDto = DetailUnidadDto.fromEntity(unidad);
        return new ResponseEntity<>(detailUnidadDto, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UpdateUnidadDto request)
            throws ConflictException, RecordNotFoundException {

        log.info("actualizando la unidad con request: {}", request);
        unidadService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
