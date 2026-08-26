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

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateMedidorDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.request.UpdateMedidorDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.response.DetailMedidorDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.response.MedidorPaginaDto;
import com.dataverse.dataverserentals.administrations.entities.Medidor;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.administrations.services.MedidorService;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.core.dto.CreateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/units/{unidadId}/meters")
@Slf4j
public class MedidorController {

    private final MedidorService medidorService;

    @GetMapping
    public ResponseEntity<Page<MedidorPaginaDto>> findAll(Pageable pageable, @RequestParam String q) {
        log.info("Listando medidores con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<MedidorPaginaDto> medidorPage = medidorService.findAll(pageable, q)
                .map(MedidorPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(medidorPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@PathVariable Long unidadId, @RequestBody CreateMedidorDto request)
            throws ConflictException {
        log.info("Iniciando la creación de una nuevo medidor: {}", request);
        Long id = medidorService.create(request, unidadId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{medidorId}")
    public ResponseEntity<DetailMedidorDto> findById(@PathVariable Long medidorId) throws RecordNotFoundException {
        log.info("Consultando detalles del medidor con ID: {}", medidorId);
        Medidor medidor = medidorService.findById(medidorId);
        DetailMedidorDto detailMedidorDto = DetailMedidorDto.fromEntity(medidor);
        return new ResponseEntity<>(detailMedidorDto, HttpStatus.OK);
    }

    @PutMapping("{medidorId}")
    public ResponseEntity<Void> update(@PathVariable Long unidadId, @PathVariable Long medidorId,
            @RequestBody UpdateMedidorDto request) throws RecordNotFoundException, ConflictException {
        log.info("Iniciando actualización del medidor: {}", request);
        medidorService.update(request, unidadId, medidorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
