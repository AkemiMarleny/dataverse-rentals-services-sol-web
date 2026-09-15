package com.dataverse.dataverserentals.gestionoperativa.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.CreateResponse;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateGastoOperativoDto;
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

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateGastoOperativoDto request)
            throws RecordNotFoundException {

        log.info("Iniciando la creación de un nuevo gasto operativo: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = gastoOperativoService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }
}
