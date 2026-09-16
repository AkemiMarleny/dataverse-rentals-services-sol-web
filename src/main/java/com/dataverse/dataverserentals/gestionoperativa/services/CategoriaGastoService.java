package com.dataverse.dataverserentals.gestionoperativa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateCategoriaGastoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.UpdateCategoriaGastoDto;
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

    public Page<CategoriaGasto> findAll(Pageable pageable, String q) {

        log.info("Búsqueda paginada de categorías de gasto [filtro: '{}', pageable: {}]", q, pageable);

        if (q == null || q.isBlank()) {
            return categoriaGastoRepository.findAll(pageable);
        }

        return categoriaGastoRepository.findByNombreContainingIgnoreCase(pageable, q);
    }

    public Long create(@Valid CreateCategoriaGastoDto payload, Long usuarioId) throws ConflictException {
        log.info("Registrando nueva categoría de gasto: {}", payload);

        if (categoriaGastoRepository.existsByNombreIgnoreCase(payload.nombre())) {
            throw new ConflictException(String.format("La categoría con el nombre '%s' ya existe", payload.nombre()));
        }

        CategoriaGasto categoriaGasto = CategoriaGasto.from(payload, usuarioId);

        return categoriaGastoRepository.save(categoriaGasto).getId();
    }

    public CategoriaGasto findById(Long categoriaGastoId) throws RecordNotFoundException {
        log.info("Consultando categoría de gasto con ID: {}", categoriaGastoId);
        return categoriaGastoRepository.findById(categoriaGastoId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("La categoría de gasto con id %s no se encuentra registrada.",
                                categoriaGastoId)));
    }

    public void update(Long categoriaGastoId, @Valid UpdateCategoriaGastoDto payload, Long usuarioId)
            throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización de la categoría de gasto con ID: {}. Datos: {}", categoriaGastoId, payload);
        CategoriaGasto categoriaGasto = findById(categoriaGastoId);

        Long cantidadDuplicados = categoriaGastoRepository.countByNombreIgnoreCaseAndIdNot(payload.nombre(),
                categoriaGastoId);

        if (cantidadDuplicados > 0) {
            log.info("Conflicto de actualización: El nombre '{}' ya está en uso por otra categoría", payload.nombre());
            throw new ConflictException(
                    String.format("No se puede actualizar, ya existe una categoría con el nombre '%s'",
                            payload.nombre()));
        }

        categoriaGasto.updateWith(payload, usuarioId);
        categoriaGastoRepository.save(categoriaGasto);
    }
}