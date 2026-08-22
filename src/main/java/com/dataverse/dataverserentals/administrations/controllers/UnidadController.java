package com.dataverse.dataverserentals.administrations.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateUnidadDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.response.UnidadPaginaDto;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.services.UnidadService;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.core.dto.CreateResponse;

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

}
