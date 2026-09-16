package com.dataverse.dataverserentals.gestionoperativa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.EdificioRepository;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateGastoOperativoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.UpdateGastoOperativoDto;
import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;
import com.dataverse.dataverserentals.gestionoperativa.repositories.CategoriaGastoRepository;
import com.dataverse.dataverserentals.gestionoperativa.repositories.GastoOperativoRepository;
import com.dataverse.dataverserentals.gestionoperativa.repositories.specifications.GastoOperativoSpecifications;
import com.dataverse.dataverserentals.notifications.NotificationService;

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
    private final NotificationService notificationService;

    public Page<GastoOperativo> findAll(Pageable pageable, Long edificioId, Long categoriaGastoId) {

        log.info("Búsqueda paginada de gastos operativos [edificioId: {}, categoriaId: {}, pageable: {}]",
                edificioId, categoriaGastoId, pageable);

        if (edificioId == null && categoriaGastoId == null) {
            return gastoOperativoRepository.findAll(pageable);
        }

        return gastoOperativoRepository.findAll(
                GastoOperativoSpecifications.filterBy(edificioId, categoriaGastoId),
                pageable);
    }

    public Long create(@Valid CreateGastoOperativoDto payload, Long usuarioId) throws FieldConstraintException {
        log.info("Registrando nuevo gasto operativo: {}", payload);

        if (!edificioRepository.existsById(payload.edificioId())) {
            throw new FieldConstraintException("edificioId",
                    "No se encontró el edificio solicitado.");
        }

        if (!categoriaGastoRepository.existsById(payload.categoriaGastoId())) {
            throw new FieldConstraintException("categoriaGastoId",
                    "No se encontró la categoría de gasto solicitada.");
        }

        GastoOperativo gastoOperativo = GastoOperativo.from(payload, usuarioId);

        Long gastoId = gastoOperativoRepository.save(gastoOperativo).getId();
        notificationService.sendEvent("EXPENSE_CREATED", gastoOperativo);
        return gastoId;
    }

    public void update(Long gastoOperativoId, @Valid UpdateGastoOperativoDto payload, Long usuarioId)
            throws RecordNotFoundException, FieldConstraintException {

        log.info("Iniciando actualización del gasto operativo con ID: {}. Datos: {}", gastoOperativoId, payload);

        if (!edificioRepository.existsById(payload.edificioId())) {
            throw new FieldConstraintException("edificioId",
                    "No se encontró el edificio solicitado.");
        }

        if (!categoriaGastoRepository.existsById(payload.categoriaGastoId())) {
            throw new FieldConstraintException("categoriaGastoId",
                    "No se encontró la categoría de gasto solicitada.");
        }

        GastoOperativo gastoOperativo = findById(gastoOperativoId);

        gastoOperativo.updateWith(payload, usuarioId);

        gastoOperativoRepository.save(gastoOperativo);
    }

    public GastoOperativo findById(Long gastoOperativoId) throws RecordNotFoundException {
        log.info("Consultando gasto operativo con ID: {}", gastoOperativoId);
        return gastoOperativoRepository.findById(gastoOperativoId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("El gasto operativo con id %s no se encuentra registrado.", gastoOperativoId)));
    }
}
