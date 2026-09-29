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
import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateUsuarioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateUsuarioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailUsuarioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.UsuarioPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.services.UsuarioService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usuarios")
@Slf4j
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar usuarios",
            description = "Devuelve los usuarios paginados, con búsqueda opcional por nombre de usuario o persona.")
    @ApiResponse(responseCode = "200", description = "Página de usuarios")
    public ResponseEntity<Page<UsuarioPaginaDto>> findAll(
            Pageable pageable,
            @Parameter(description = "Texto de búsqueda (nombre de usuario o persona)", example = "admin")
            @RequestParam(required = false) String q) {

        log.info("Listando usuarios con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<UsuarioPaginaDto> usuarioPage = usuarioService
                .findAll(pageable, q)
                .map(UsuarioPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(usuarioPage));
    }

    @GetMapping("{usuarioId}")
    @Operation(
            summary = "Obtener usuario por ID",
            description = "Devuelve el detalle de un usuario del sistema.")
    @ApiResponse(responseCode = "200", description = "Detalle del usuario")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<DetailUsuarioDto> findById(@PathVariable Long usuarioId)
            throws RecordNotFoundException, AccessDeniedException {

        log.info("Consultando detalles del usuario con ID: {}", usuarioId);
        Usuario usuario = usuarioService.findById(usuarioId, securityContext.getUsuarioPrincipal());
        DetailUsuarioDto detailUsuarioDto = DetailUsuarioDto.fromEntity(usuario);
        return new ResponseEntity<>(detailUsuarioDto, HttpStatus.OK);
    }

    @PostMapping
    @Operation(
            summary = "Crear usuario",
            description = "Registra un nuevo usuario del sistema asociado a una persona existente.")
    @ApiResponse(responseCode = "201", description = "Usuario creado; se devuelve el ID del usuario")
    @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. nombre de usuario ya existente)")
    @ApiResponse(responseCode = "422", description = "Datos del usuario no procesables")
    public ResponseEntity<CreateResponse> create(@RequestBody CreateUsuarioDto request)
            throws RecordNotFoundException, ConflictException, UnprocesableEntity {

        log.info("Iniciando el registro de un nuevo usuario: {}", request);
        Long id = usuarioService.create(
                request,
                securityContext.getUsuarioPrincipal());

        return new ResponseEntity<>(
                new CreateResponse(id), HttpStatus.CREATED);
    }

    @PutMapping("{usuarioId}")
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza el rol, estado y datos de un usuario existente.")
    @ApiResponse(responseCode = "204", description = "Usuario actualizado correctamente")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. nombre de usuario ya existente)")
    public ResponseEntity<Void> actualizarUsuario(
            @PathVariable Long usuarioId,
            @RequestBody @Valid UpdateUsuarioDto request)
            throws RecordNotFoundException, AccessDeniedException, ConflictException {

        log.info("Iniciando actualización del usuario con ID: {}", usuarioId);
        usuarioService.actualizarUsuario(usuarioId, request, securityContext.getUsuarioPrincipal());
        return ResponseEntity.noContent().build();
    }
}