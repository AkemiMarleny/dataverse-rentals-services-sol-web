package com.dataverse.dataverserentals.administrations.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateMedidorDto;
import com.dataverse.dataverserentals.administrations.entities.Medidor;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.administrations.repositories.MedidorRepository;
import com.dataverse.dataverserentals.administrations.repositories.UnidadRepository;
import com.dataverse.dataverserentals.administrations.repositories.specifications.MedidorSpecifications;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class MedidorService {

    private final MedidorRepository medidorRepository;
    private final UnidadRepository unidadRepository;

    public Page<Medidor> findAll(Pageable pageable, String q) {
        return medidorRepository.findAll(MedidorSpecifications.tipoMedidorOrNumeroSerieContains(q), pageable);
    }

    public Long create(@Valid CreateMedidorDto payload, Long unidadId) throws ConflictException {
        log.info("Proceso de creación del medidor iniciado con la solicitud: {}", payload);
        unidadRepository.findById(unidadId).orElseThrow(() -> new ConflictException(String.format(
                "La unidad solicitada con el id: %s no se encuentra registrada.", unidadId)));

        if (medidorRepository.existsByUnidadIdAndTipoMedidorCode(unidadId, payload.tipoMedidorCode())) {
            throw new ConflictException(String.format(
                    "Ya existe un medidor de tipo %s para la unidad %s",
                    payload.tipoMedidorCode(), unidadId));
        }

        if (medidorRepository.existsByNumeroSerie(payload.numeroSerie())) {
            throw new ConflictException(String.format(
                    "El número de serie %s ya está registrada en el sistema",
                    payload.numeroSerie()));
        }

        Medidor medidor = Medidor.from(payload, unidadId);
        return medidorRepository.save(medidor).getId();

    }

    public Medidor findById(Long medidorId) throws RecordNotFoundException {
        return medidorRepository.findById(medidorId).orElseThrow(() -> new RecordNotFoundException(
                String.format("El medidor solictado con el id: %s no se encuentra registrado.", medidorId)));
    }

}
