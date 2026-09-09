package com.dataverse.dataverserentals.gestionfinanciera.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response.PagoDto;
import com.dataverse.dataverserentals.gestionfinanciera.services.PagoService;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

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
}
