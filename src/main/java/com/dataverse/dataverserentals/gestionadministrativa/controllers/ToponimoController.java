package com.dataverse.dataverserentals.gestionadministrativa.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.ToponimoPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.services.ToponimoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/toponimos")
@RequiredArgsConstructor
@Slf4j
public class ToponimoController {

    private final ToponimoService toponimoService;

    @GetMapping
    public ResponseEntity<Page<ToponimoPaginaDto>> findAll(Pageable pageable, @RequestParam String q)
            throws AccessDeniedException {

        log.info("Listando topónimos con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<ToponimoPaginaDto> toponimoPage = toponimoService.findAll(pageable, q)
                .map(ToponimoPaginaDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(toponimoPage));
    }

}
