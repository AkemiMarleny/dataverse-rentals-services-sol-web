package com.dataverse.dataverserentals.gestionfinanciera.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dataverse.dataverserentals.common.dto.CreateResponse;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.request.AuditarPagoDto;
import com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.request.CreatePagoDto;
import com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response.PagoDto;
import com.dataverse.dataverserentals.gestionfinanciera.services.PagoService;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recibos/{reciboId}/pagos")
@Tag(name = "Pagos", description = "Registro, consulta y auditoría de pagos de recibos")
public class PagoController {

    private final PagoService pagoService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar pagos de un recibo",
            description = "Devuelve los pagos registrados de un recibo, paginados.")
    @ApiResponse(responseCode = "200", description = "Página de pagos")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Recibo no encontrado")
    public ResponseEntity<Page<PagoDto>> findPagosByReciboId(
            @PathVariable Long reciboId,
            Pageable pageable) throws AccessDeniedException, RecordNotFoundException {
        log.info("Listando pagos del recibo con ID: {}", reciboId);

        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();
        var pagosPage = pagoService.findPagosByReciboId(
                reciboId, usuarioPrincipal, pageable)
                .map(PagoDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(pagosPage));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Registrar pago",
            description = "Registra un pago para el recibo. Envía un formulario multipart con la parte 'pago' (JSON con los datos del pago) y la parte 'file' (voucher) opcional.")
    @ApiResponse(responseCode = "201", description = "Pago registrado; se devuelve el ID del pago creado")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Recibo no encontrado")
    @ApiResponse(responseCode = "422", description = "Datos del pago inválidos o no procesables")
    public ResponseEntity<CreateResponse> createPago(
            @PathVariable Long reciboId,
            @RequestPart("pago") @Valid CreatePagoDto request,
            @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        log.info("Registrando pago para recibo con ID: {}, monto: {}", reciboId, request.montoPagado());

        if (file != null && !file.isEmpty()) {
            log.info("Archivo recibido: {} ({} bytes)", file.getOriginalFilename(), file.getSize());
        }

        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();
        Long pagoId = pagoService.createPago(reciboId, request, file, usuarioPrincipal);
        return new ResponseEntity<>(new CreateResponse(pagoId), HttpStatus.CREATED);
    }

    @PutMapping("/{pagoId}/auditar")
    @Operation(
            summary = "Auditar pago",
            description = "Aprueba o rechaza un pago registrado, con el motivo en caso de rechazo.")
    @ApiResponse(responseCode = "204", description = "Pago auditado correctamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida (p. ej. campos de auditoría incorrectos)")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "422", description = "Datos de auditoría no procesables")
    public ResponseEntity<Void> auditarPago(
            @PathVariable Long reciboId,
            @PathVariable Long pagoId,
            @RequestBody @Valid AuditarPagoDto request)
            throws FieldConstraintException, AccessDeniedException, UnprocesableEntity {
        log.info("Auditando pago ID: {} del recibo ID: {}, accion: {}", pagoId, reciboId, request.accion());

        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();
        pagoService.auditarPago(reciboId, pagoId, request, usuarioPrincipal);
        return ResponseEntity.noContent().build();
    }
}