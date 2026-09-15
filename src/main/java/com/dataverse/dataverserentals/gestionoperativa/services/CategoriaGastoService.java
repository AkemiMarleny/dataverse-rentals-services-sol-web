package com.dataverse.dataverserentals.gestionoperativa.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateCategoriaGastoDto;
import com.dataverse.dataverserentals.gestionoperativa.entities.CategoriaGasto;
import com.dataverse.dataverserentals.gestionoperativa.repositories.CategoriaGastoRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoriaGastoService {

    private final CategoriaGastoRepository categoriaGastoRepository;

    public Long create(@Valid CreateCategoriaGastoDto payload, Long usuarioId) throws ConflictException {
        log.info("Registrando nueva categoría de gasto: {}", payload);

        if (categoriaGastoRepository.existsByNombreIgnoreCase(payload.nombre())) {
            throw new ConflictException(String.format("La categoría con el nombre '%s' ya existe", payload.nombre()));
        }

        CategoriaGasto categoriaGasto = CategoriaGasto.from(payload, usuarioId);

        return categoriaGastoRepository.save(categoriaGasto).getId();
    }
}