package com.dataverse.dataverserentals.gestionoperativa.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.CreateResponse;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateCategoriaGastoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.CategoriaGastoPaginaDto;
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

    @GetMapping
    public ResponseEntity<Page<CategoriaGastoPaginaDto>> findAll(Pageable pageable,
            @RequestParam(required = false) String q) {

        log.info("Listando categorías de gasto con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<CategoriaGastoPaginaDto> categoriasPage = categoriaGastoService
                .findAll(pageable, q)
                .map(CategoriaGastoPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(categoriasPage));
    }

    @PostMapping
    public ResponseEntity<CreateResponse> create(@RequestBody CreateCategoriaGastoDto request) throws ConflictException {

        log.info("Iniciando la creación de una nueva categoría de gasto: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = categoriaGastoService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

}