package com.dataverse.dataverserentals.gestionadministrativa.controllers;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dataverse.dataverserentals.common.dto.CreateResponse;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateContratoDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.ContratoPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailContratoDto;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoContrato;
import com.dataverse.dataverserentals.gestionadministrativa.services.ContratoService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/contratos")
@Slf4j
@Tag(name = "Contratos", description = "Gestión de contratos de alquiler")
public class ContratoController {

    private final ContratoService contratoService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar contratos",
            description = "Devuelve los contratos paginados, con filtros opcionales por estado y búsqueda.")
    @ApiResponse(responseCode = "200", description = "Página de contratos")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    public ResponseEntity<Page<ContratoPaginaDto>> findAll(
            Pageable pageable,
            @Parameter(description = "Filtro por estado del contrato", example = "VIGENTE")
            @RequestParam Optional<EstadoContrato> estadoContrato,
            @Parameter(description = "Texto de búsqueda (persona o unidad)", example = "Juan")
            @RequestParam(defaultValue = "") String q) throws AccessDeniedException {

        log.info("Listando contratos con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<ContratoPaginaDto> contratoPage = contratoService
                .findAll(pageable, q, estadoContrato)
                .map(ContratoPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(contratoPage));
    }

    @PostMapping
    @Operation(
            summary = "Crear contrato",
            description = "Registra un nuevo contrato de alquiler entre una unidad y una persona.")
    @ApiResponse(responseCode = "201", description = "Contrato creado; se devuelve el ID del contrato")
    @ApiResponse(responseCode = "404", description = "Unidad o persona no encontrada")
    @ApiResponse(responseCode = "422", description = "Datos del contrato no procesables")
    public ResponseEntity<CreateResponse> create(@RequestBody CreateContratoDto request)
            throws RecordNotFoundException, UnprocesableEntity {

        log.info("Iniciando la creación de un nuevo contrato: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = contratoService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{contratoId}")
    @Operation(
            summary = "Obtener contrato por ID",
            description = "Devuelve el detalle de un contrato, incluyendo datos de la unidad y la persona.")
    @ApiResponse(responseCode = "200", description = "Detalle del contrato")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
    public ResponseEntity<DetailContratoDto> findById(@PathVariable Long contratoId)
            throws AccessDeniedException, RecordNotFoundException {
        log.info("Recuperando detalle del contrato con ID: {}", contratoId);
        var contrato = contratoService.findById(contratoId);
        return new ResponseEntity<>(DetailContratoDto.fromEntity(contrato), HttpStatus.OK);
    }

    @PostMapping(value = "{contratoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Subir PDF del contrato",
            description = "Adjunta el PDF digitalizado del contrato. Envía el archivo en la parte 'file' del formulario multipart.")
    @ApiResponse(responseCode = "204", description = "PDF adjuntado correctamente")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
    public ResponseEntity<Void> uploadContratto(
            @PathVariable Long contratoId,
            @RequestPart(value = "file", required = true) MultipartFile file)
            throws AccessDeniedException, RecordNotFoundException {

        contratoService.uploadContratto(contratoId, file, securityContext.getUsuarioPrincipal());
        return ResponseEntity.noContent().build();
    }
}