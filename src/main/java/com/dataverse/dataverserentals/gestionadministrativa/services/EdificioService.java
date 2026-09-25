package com.dataverse.dataverserentals.gestionadministrativa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateBuildingAddress;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateEdificioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateEdificioDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Direccion;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.DireccionRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.EdificioRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.ToponimoRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class EdificioService {

    private final EdificioRepository edificioRepository;
    private final ToponimoRepository toponimoRepository;
    private final DireccionRepository direccionRepository;

    public Page<Edificio> findAll(Pageable pageable, String q) {

        log.info("Búsqueda paginada de edificios [filtro: '{}', pageable: {}]", q, pageable);

        if (q == null || q.isBlank()) {
            return edificioRepository.findAll(pageable);
        }

        return edificioRepository.findByNombreContainingIgnoreCase(pageable, q);
    }

    public Long create(@Valid CreateEdificioDto payload, Long usuarioId) throws ConflictException {

        log.info("Registrando nuevo edificio con los siguientes datos: {}", payload);
        if (edificioRepository.existsByNombreIgnoreCase(payload.nombre())) {
            throw new ConflictException(String.format("Ya existe un edificio con el nombre: %s", payload.nombre()));
        }
        Edificio edificio = Edificio.from(payload, usuarioId);
        return edificioRepository.save(edificio).getId();
    }

    public Edificio findById(Long edificioId) throws RecordNotFoundException {

        log.info("Recuperando datos del edificio con identificador: {}", edificioId);
        return edificioRepository.findById(edificioId).orElseThrow(() -> new RecordNotFoundException(
                String.format("El edificio solicitado con el id: %s no se encuentra registrado.", edificioId)));
    }

    public void update(Long edificioId, @Valid UpdateEdificioDto payload, Long usuarioId)
            throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización del edificio con ID: {}. Datos recibidos: {}", edificioId, payload);
        Edificio edificio = findById(edificioId);

        Long cantidadEdificiosDuplicados = edificioRepository.countByNombreIgnoreCaseAndIdNot(payload.nombre(),
                edificioId);
        if (cantidadEdificiosDuplicados > 0) {
            log.info("Validación fallida: Ya existe otro edificio con el mismo nombre: {}", payload.nombre());
            throw new ConflictException(
                    String.format("No se puede actualizar: ya existe este nombre registrado: %s",
                            payload.nombre()));
        }

        edificio.updateWith(payload, usuarioId);
        edificioRepository.save(edificio);
    }

    @Transactional
    public Long createBuildingAddress(Long edificioId, @Valid CreateBuildingAddress payload)
            throws RecordNotFoundException {

        log.info("Registrando nueva dirección para el edificio ID: {} con los datos: {}", edificioId, payload);

        findById(edificioId);

        toponimoRepository.findById(payload.toponimoId())
                .orElseThrow(() -> new FieldConstraintException("toponimoId", String
                        .format("El tipo de topónimo con id: %s que ha ingresado no se encuentra registrado.",
                                payload.toponimoId())));

        Direccion direccion = Direccion.from(payload);
        return direccionRepository.save(direccion).getId();

    }

}
