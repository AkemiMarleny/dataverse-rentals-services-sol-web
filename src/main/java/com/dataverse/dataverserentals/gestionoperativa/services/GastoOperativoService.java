package com.dataverse.dataverserentals.gestionoperativa.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.EdificioRepository;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateGastoOperativoDto;
import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;
import com.dataverse.dataverserentals.gestionoperativa.repositories.CategoriaGastoRepository;
import com.dataverse.dataverserentals.gestionoperativa.repositories.GastoOperativoRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class GastoOperativoService {

    private final GastoOperativoRepository gastoOperativoRepository;
    private final EdificioRepository edificioRepository;
    private final CategoriaGastoRepository categoriaGastoRepository;

    public Long create(@Valid CreateGastoOperativoDto payload, Long usuarioId) throws RecordNotFoundException {
        log.info("Registrando nuevo gasto operativo: {}", payload);

        if (!edificioRepository.existsById(payload.edificioId())) {
            throw new RecordNotFoundException("No se encontró el edificio con ID: " + payload.edificioId());
        }

        if (!categoriaGastoRepository.existsById(payload.categoriaGastoId())) {
            throw new RecordNotFoundException("No se encontró la categoría de gasto con ID: " + payload.categoriaGastoId());
        }

        GastoOperativo gastoOperativo = GastoOperativo.from(payload, usuarioId);

        return gastoOperativoRepository.save(gastoOperativo).getId();
    }
}