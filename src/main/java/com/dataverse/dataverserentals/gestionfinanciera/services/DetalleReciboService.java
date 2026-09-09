package com.dataverse.dataverserentals.gestionfinanciera.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionfinanciera.entities.DetalleRecibo;
import com.dataverse.dataverserentals.gestionfinanciera.repositories.DetalleReciboRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class DetalleReciboService {

    private final DetalleReciboRepository detalleReciboRepository;
    private final ReciboService reciboService;

    public Page<DetalleRecibo> findDetallesByReciboId(Long reciboId, Pageable pageable)
            throws AccessDeniedException, RecordNotFoundException {
        var recibo = reciboService.findReciboById(reciboId);

        return detalleReciboRepository.findByReciboId(recibo.getId(), pageable);
    }
}
