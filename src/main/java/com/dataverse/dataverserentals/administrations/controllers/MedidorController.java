package com.dataverse.dataverserentals.administrations.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateMedidorDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.response.MedidorPaginaDto;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.services.MedidorService;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.core.dto.CreateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/units/{unitId}/meters")
@Slf4j
public class MedidorController {

    private final MedidorService medidorService;

    @GetMapping
    public ResponseEntity<Page<MedidorPaginaDto>> findAll(Pageable pageable, @RequestParam String q) {
        org.springframework.data.domain.Page<MedidorPaginaDto> medidorPage = medidorService.findAll(pageable, q)
                .map(MedidorPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(medidorPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@PathVariable Long unidadId, @RequestBody CreateMedidorDto request)
            throws ConflictException {
        log.info("Creando un nuevo medidor con solicitud: {}", request);
        Long id = medidorService.create(request, unidadId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

}
