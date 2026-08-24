package com.dataverse.dataverserentals.administrations.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateMedidorDto;
import com.dataverse.dataverserentals.administrations.entities.Medidor;
import com.dataverse.dataverserentals.administrations.exceptions.ConflictException;
import com.dataverse.dataverserentals.administrations.repositories.MedidorRepository;
import com.dataverse.dataverserentals.administrations.repositories.UnidadRepository;

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

    public Long create(@Valid CreateMedidorDto payload) throws ConflictException {
        log.info("Proceso de creación del medidor iniciado con la solicitud: {}", payload);
        unidadRepository.findById(payload.unidadId()).orElseThrow(() -> new ConflictException(String.format(
                "La unidad solicitada con el id: %s no se encuentra registrada.", payload.unidadId())));

        if (medidorRepository.existsByUnidadIdAndTipoMedidor(payload.unidadId(), payload.tipoMedidor())) {
            throw new ConflictException(String.format(
                    "Ya existe un medidor de tipo %s para la unidad %s",
                    payload.tipoMedidor(), payload.unidadId()));
        }

        if (medidorRepository.existsByNumeroSerie(payload.numeroSerie())) {
            throw new ConflictException(String.format(
                    "El número de serie %s ya está registrada en el sistema",
                    payload.numeroSerie()));
        }

        Medidor medidor = Medidor.from(payload);
        return medidorRepository.save(medidor).getId();

    }

}
