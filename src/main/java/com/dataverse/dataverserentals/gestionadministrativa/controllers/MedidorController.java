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
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateMedidorDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateMedidorDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailMedidorDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.MedidorPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.services.MedidorService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/edificios/{edificioId}/unidades/{unidadId}/medidores")
@Slf4j
public class MedidorController {

    private final MedidorService medidorService;
    private final SecurityContext securityContext;

    @GetMapping
    public ResponseEntity<Page<MedidorPaginaDto>> findAll(Pageable pageable, @PathVariable Long unidadId,
            @RequestParam(required = false) String q) {

        log.info("Listando medidores con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<MedidorPaginaDto> medidorPage = medidorService
                .findAll(pageable, unidadId, q)
                .map(MedidorPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(medidorPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@PathVariable Long edificioId, @PathVariable Long unidadId,
            @RequestBody CreateMedidorDto request)
            throws ConflictException, RecordNotFoundException {

        log.info("Iniciando la creación de una nuevo medidor: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = medidorService.create(request, edificioId, unidadId, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{medidorId}")
    public ResponseEntity<DetailMedidorDto> findById(
            @PathVariable Long edificioId,
            @PathVariable Long unidadId,
            @PathVariable Long medidorId) throws RecordNotFoundException {

        log.info("Consultando detalles del medidor con ID: {}", medidorId);
        Medidor medidor = medidorService.findById(edificioId, unidadId, medidorId);
        DetailMedidorDto detailMedidorDto = DetailMedidorDto.fromEntity(medidor);
        return new ResponseEntity<>(detailMedidorDto, HttpStatus.OK);
    }

    @PutMapping("{medidorId}")
    public ResponseEntity<Void> update(
            @PathVariable Long edificioId,
            @PathVariable Long unidadId,
            @PathVariable Long medidorId,
            @RequestBody UpdateMedidorDto request) throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización del medidor: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        medidorService.update(request, edificioId, unidadId, medidorId, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
