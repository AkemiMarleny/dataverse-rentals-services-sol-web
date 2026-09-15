package com.dataverse.dataverserentals.gestionoperativa.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.CreateResponse;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateCategoriaGastoDto;
import com.dataverse.dataverserentals.gestionoperativa.services.CategoriaGastoService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categorias-gastos")
@Slf4j
public class CategoriaGastoController {

    private final CategoriaGastoService categoriaGastoService;
    private final SecurityContext securityContext;

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateCategoriaGastoDto request) throws ConflictException {

        log.info("Iniciando la creación de una nueva categoría de gasto: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = categoriaGastoService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

}