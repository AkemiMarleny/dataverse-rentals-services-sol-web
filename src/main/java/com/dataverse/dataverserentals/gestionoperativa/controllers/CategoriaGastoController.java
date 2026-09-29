package com.dataverse.dataverserentals.gestionoperativa.controllers;

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
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateCategoriaGastoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.UpdateCategoriaGastoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.CategoriaGastoPaginaDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.DetailCategoriaGastoDto;
import com.dataverse.dataverserentals.gestionoperativa.entities.CategoriaGasto;
import com.dataverse.dataverserentals.gestionoperativa.services.CategoriaGastoService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categorias-gastos")
@Slf4j
@Tag(name = "Categorías de gasto", description = "Gestión de categorías de gastos operativos")
public class CategoriaGastoController {

    private final CategoriaGastoService categoriaGastoService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar categorías de gasto",
            description = "Devuelve las categorías de gasto paginadas, con búsqueda opcional por nombre o descripción.")
    @ApiResponse(responseCode = "200", description = "Página de categorías de gasto")
    public ResponseEntity<Page<CategoriaGastoPaginaDto>> findAll(Pageable pageable,
            @Parameter(description = "Texto de búsqueda (nombre o descripción)", example = "mantenimiento")
            @RequestParam(required = false) String q) {

        log.info("Listando categorías de gasto con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<CategoriaGastoPaginaDto> categoriasPage = categoriaGastoService
                .findAll(pageable, q)
                .map(CategoriaGastoPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(categoriasPage));
    }

    @PostMapping
    @Operation(
            summary = "Crear categoría de gasto",
            description = "Registra una nueva categoría de gasto con su nombre y descripción.")
    @ApiResponse(responseCode = "201", description = "Categoría creada; se devuelve el ID de la categoría")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. nombre de categoría ya existente)")
    public ResponseEntity<CreateResponse> create(@RequestBody CreateCategoriaGastoDto request)
            throws ConflictException {

        log.info("Iniciando la creación de una nueva categoría de gasto: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = categoriaGastoService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{categoriaGastoId}")
    @Operation(
            summary = "Obtener categoría de gasto por ID",
            description = "Devuelve el detalle de una categoría de gasto.")
    @ApiResponse(responseCode = "200", description = "Detalle de la categoría de gasto")
    @ApiResponse(responseCode = "404", description = "Categoría de gasto no encontrada")
    public ResponseEntity<DetailCategoriaGastoDto> findById(@PathVariable Long categoriaGastoId)
            throws RecordNotFoundException {

        log.info("Consultando detalles de la categoría de gasto con ID: {}", categoriaGastoId);
        CategoriaGasto categoriaGasto = categoriaGastoService.findById(categoriaGastoId);
        DetailCategoriaGastoDto detailCategoriaGastoDto = DetailCategoriaGastoDto.fromEntity(categoriaGasto);
        return new ResponseEntity<>(detailCategoriaGastoDto, HttpStatus.OK);
    }

    @PutMapping("{categoriaGastoId}")
    @Operation(
            summary = "Actualizar categoría de gasto",
            description = "Actualiza el nombre y descripción de una categoría de gasto existente.")
    @ApiResponse(responseCode = "204", description = "Categoría actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Categoría de gasto no encontrada")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. nombre de categoría ya existente)")
    public ResponseEntity<Void> update(
            @PathVariable Long categoriaGastoId,
            @RequestBody UpdateCategoriaGastoDto request) throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización de la categoría de gasto con ID: {}", categoriaGastoId);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        categoriaGastoService.update(categoriaGastoId, request, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}