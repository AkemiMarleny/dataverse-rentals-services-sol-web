package com.dataverse.dataverserentals.gestioncampo.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.CreateResponse;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.CreateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.UpdateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.response.DetailLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.response.LecturaHistoricaPaginaDto;
import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;
import com.dataverse.dataverserentals.gestioncampo.services.LecturaHistoricaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/medidores/{medidorId}/lecturas-historicas")
@Slf4j
public class LecturaHistoricaController {

    private final LecturaHistoricaService lecturaHistoricaService;

    @GetMapping
    public ResponseEntity<Page<LecturaHistoricaPaginaDto>> findAll(@PathVariable Long medidorId, Pageable pageable) {

        log.info("Listando las lecturas históricas del medidor ID: {} con parámetros: {}", medidorId, pageable);

        org.springframework.data.domain.Page<LecturaHistoricaPaginaDto> lecturaHistoricaPage = lecturaHistoricaService
                .findAll(medidorId, pageable)
                .map(LecturaHistoricaPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(lecturaHistoricaPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@PathVariable Long medidorId,
            @RequestBody CreateLecturaHistoricaDto request)
            throws FieldConstraintException {

        log.info("Creando una lectura histórica: medidorId={}, valor={}",
                medidorId, request.valorLectura());

        Long lecturaHistoricaId = lecturaHistoricaService.create(medidorId, request);
        return new ResponseEntity<>(new CreateResponse(lecturaHistoricaId), HttpStatus.CREATED);
    }

    @GetMapping("{lecturaHistoricaId}")
    public ResponseEntity<DetailLecturaHistoricaDto> findById(@PathVariable Long lecturaHistoricaId)
            throws RecordNotFoundException {

        log.info("Detalles de lectura histórica recuperados exitosamente para el ID: {}", lecturaHistoricaId);

        LecturaHistorica lecturaHistorica = lecturaHistoricaService.findById(lecturaHistoricaId);
        DetailLecturaHistoricaDto detailLecturaHistoricaDto = DetailLecturaHistoricaDto.fromEntity(lecturaHistorica);
        return new ResponseEntity<>(detailLecturaHistoricaDto, HttpStatus.OK);
    }

    @PutMapping("{lecturaHistoricaId}")
    public ResponseEntity<Void> update(@PathVariable Long medidorId, @PathVariable Long lecturaHistoricaId,
            @RequestBody UpdateLecturaHistoricaDto request) throws RecordNotFoundException {

        log.info("Iniciando actualización de la lectura histórica con ID: {} del medidor ID: {}", lecturaHistoricaId,
                medidorId);
        lecturaHistoricaService.update(medidorId, lecturaHistoricaId, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
