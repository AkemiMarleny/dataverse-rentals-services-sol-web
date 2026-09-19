package com.dataverse.dataverserentals.gestioncampo.controllers;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.CreateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.UpdateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.response.DetailLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.response.LecturaHistoricaPaginaDto;
import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;
import com.dataverse.dataverserentals.gestioncampo.services.LecturaHistoricaService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/lecturas-historicas")
@Slf4j
public class LecturaHistoricaController {

    private final LecturaHistoricaService lecturaHistoricaService;
    private final SecurityContext securityContext;

    @GetMapping
    public ResponseEntity<Page<LecturaHistoricaPaginaDto>> findAll(
            @RequestParam(required = false) Long medidorId,
            Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        log.info("Listando lecturas históricas con filtros: medidorId={}, fechaDesde={}, fechaHasta={}, parámetros: {}", 
                medidorId, fechaDesde, fechaHasta, pageable);

        org.springframework.data.domain.Page<LecturaHistoricaPaginaDto> lecturaHistoricaPage = lecturaHistoricaService
                .findAll(medidorId, pageable, fechaDesde, fechaHasta)
                .map(LecturaHistoricaPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(lecturaHistoricaPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(
            @RequestBody CreateLecturaHistoricaDto request)
            throws FieldConstraintException {

        log.info("Iniciando la creación de una nueva lectura histórica: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long lecturaHistoricaId = lecturaHistoricaService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(lecturaHistoricaId), HttpStatus.CREATED);
    }

    @GetMapping("{lecturaHistoricaId}")
    public ResponseEntity<DetailLecturaHistoricaDto> findById(
            @PathVariable Long lecturaHistoricaId) throws RecordNotFoundException {

        log.info("Consultando detalles de la lectura histórica con ID: {}", lecturaHistoricaId);

        LecturaHistorica lecturaHistorica = lecturaHistoricaService.findById(lecturaHistoricaId);
        DetailLecturaHistoricaDto detailLecturaHistoricaDto = DetailLecturaHistoricaDto.fromEntity(lecturaHistorica);
        return new ResponseEntity<>(detailLecturaHistoricaDto, HttpStatus.OK);
    }

    @PutMapping("{lecturaHistoricaId}")
    public ResponseEntity<Void> update(@PathVariable Long lecturaHistoricaId,
            @RequestBody UpdateLecturaHistoricaDto request) throws RecordNotFoundException {

        log.info("Iniciando actualización de la lectura histórica: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        lecturaHistoricaService.update(lecturaHistoricaId, request, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
