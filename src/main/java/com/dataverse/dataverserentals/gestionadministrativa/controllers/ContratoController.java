package com.dataverse.dataverserentals.gestionadministrativa.controllers;

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

import com.dataverse.dataverserentals.common.dto.CreateResponse;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateContratoDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.ContratoPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailContratoDto;
import com.dataverse.dataverserentals.gestionadministrativa.services.ContratoService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/contratos")
@Slf4j
public class ContratoController {

    private final ContratoService contratoService;
    private final SecurityContext securityContext;

    @GetMapping
    public ResponseEntity<Page<ContratoPaginaDto>> findAll(
            Pageable pageable,
            @RequestParam(defaultValue = "") String q) {

        log.info("Listando contratos con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<ContratoPaginaDto> contratoPage = contratoService
                .findAll(pageable, q)
                .map(ContratoPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(contratoPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateContratoDto request)
            throws RecordNotFoundException, UnprocesableEntity {

        log.info("Iniciando la creación de un nuevo contrato: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = contratoService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{contratoId}")
    public ResponseEntity<DetailContratoDto> findById(@PathVariable Long contratoId) throws RecordNotFoundException {
        log.info("Recuperando detalle del contrato con ID: {}", contratoId);
        var contrato = contratoService.findById(contratoId);
        return new ResponseEntity<>(DetailContratoDto.fromEntity(contrato), HttpStatus.OK);
    }
}
