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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recibos/{reciboId}/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final SecurityContext securityContext;

    @GetMapping
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
