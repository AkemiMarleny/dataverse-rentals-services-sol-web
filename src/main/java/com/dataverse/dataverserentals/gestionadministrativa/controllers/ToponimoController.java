package com.dataverse.dataverserentals.gestionadministrativa.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateToponimoDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateToponimoDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailToponimoDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.ToponimoPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Toponimo;
import com.dataverse.dataverserentals.gestionadministrativa.services.ToponimoService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/toponimos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Topónimos", description = "Gestión de topónimos para direcciones")
public class ToponimoController {

    private final ToponimoService toponimoService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar topónimos",
            description = "Devuelve los topónimos paginados, con búsqueda obligatoria por nombre.")
    @ApiResponse(responseCode = "200", description = "Página de topónimos")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    public ResponseEntity<Page<ToponimoPaginaDto>> findAll(Pageable pageable,
            @Parameter(description = "Texto de búsqueda (nombre del topónimo)", example = "Av. Los Alamos")
            @RequestParam String q)
            throws AccessDeniedException {

        log.info("Listando topónimos con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<ToponimoPaginaDto> toponimoPage = toponimoService.findAll(pageable, q)
                .map(ToponimoPaginaDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(toponimoPage));
    }

    @PostMapping
    @Operation(
            summary = "Crear topónimo",
            description = "Registra un nuevo topónimo (avenida, calle, jirón, etc.).")
    @ApiResponse(responseCode = "201", description = "Topónimo creado; se devuelve el ID del topónimo")
    public ResponseEntity<CreateResponse> create(@RequestBody CreateToponimoDto request) {

        log.info("Iniciando la creación de una nuevo topónimo: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = toponimoService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{toponimoId}")
    @Operation(
            summary = "Obtener topónimo por ID",
            description = "Devuelve el detalle de un topónimo.")
    @ApiResponse(responseCode = "200", description = "Detalle del topónimo")
    @ApiResponse(responseCode = "404", description = "Topónimo no encontrado")
    public ResponseEntity<DetailToponimoDto> findById(@PathVariable Long toponimoId) throws RecordNotFoundException {

        log.info("Consultando detalles del topónimo con ID: {}", toponimoId);
        Toponimo toponimo = toponimoService.findById(toponimoId);
        DetailToponimoDto detailToponimoDto = DetailToponimoDto.fromEntity(toponimo);
        return new ResponseEntity<>(detailToponimoDto, HttpStatus.OK);
    }

    @PutMapping("{toponimoId}")
    @Operation(
            summary = "Actualizar topónimo",
            description = "Actualiza el nombre de un topónimo existente.")
    @ApiResponse(responseCode = "204", description = "Topónimo actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Topónimo no encontrado")
    public ResponseEntity<Void> update(@PathVariable Long toponimoId, @RequestBody UpdateToponimoDto request)
            throws RecordNotFoundException {

        log.info("Iniciando actualización del topónimo: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        toponimoService.update(toponimoId, request, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}