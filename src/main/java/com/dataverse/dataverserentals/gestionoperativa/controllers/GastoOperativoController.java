package com.dataverse.dataverserentals.gestionoperativa.controllers;

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
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateGastoOperativoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.DetailGastoOperativoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.GastoOperativoPaginaDto;
import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;
import com.dataverse.dataverserentals.gestionoperativa.services.GastoOperativoService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/gastos-operativos")
@Slf4j
public class GastoOperativoController {

    private final GastoOperativoService gastoOperativoService;
    private final SecurityContext securityContext;

    @GetMapping
    public ResponseEntity<Page<GastoOperativoPaginaDto>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long edificioId,
            @RequestParam(required = false) Long categoriaGastoId) {

        log.info("Listando gastos operativos con edificioId: {}, categoriaId: {} y parámetros: {}",
                edificioId, categoriaGastoId, pageable);

        org.springframework.data.domain.Page<GastoOperativoPaginaDto> gastoPage = gastoOperativoService
                .findAll(pageable, edificioId, categoriaGastoId)
                .map(GastoOperativoPaginaDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(gastoPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateGastoOperativoDto request)
            throws RecordNotFoundException {

        log.info("Iniciando la creación de un nuevo gasto operativo: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = gastoOperativoService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{gastoOperativoId}")
    public ResponseEntity<DetailGastoOperativoDto> findById(@PathVariable Long gastoOperativoId)
            throws RecordNotFoundException {

        log.info("Consultando detalles del gasto operativo con ID: {}", gastoOperativoId);
        GastoOperativo gastoOperativo = gastoOperativoService.findById(gastoOperativoId);
        DetailGastoOperativoDto detailGastoOperativoDto = DetailGastoOperativoDto.fromEntity(gastoOperativo);
        return new ResponseEntity<>(detailGastoOperativoDto, HttpStatus.OK);
    }
}
