package com.dataverse.dataverserentals.gestionadministrativa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.helpers.CapitalizeWord;
import com.dataverse.dataverserentals.common.helpers.Normalizar;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateToponimoDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateToponimoDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Toponimo;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.ToponimoRepository;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ToponimoService {

    private final ToponimoRepository toponimoRepository;
    private final SecurityContext securityContext;

    public Page<Toponimo> findAll(Pageable pageable, String q) throws AccessDeniedException {

        log.info("Búsqueda paginada de topónimos [filtro: '{}', pageable: {}]", q, pageable);

        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();
        RolUsuario rol = usuarioPrincipal.usuarioRol();

        if (RolUsuario.ADMINISTRADOR.equals(rol)) {
            return toponimoRepository.findByNombreContaining(pageable, q);
        }

        throw new AccessDeniedException(usuarioPrincipal);
    }

    public Long create(@Valid CreateToponimoDto payload, Long usuarioId) {

        log.info("Registrando nuevo topónimo con los siguientes datos: {}", payload);

        Toponimo toponimo = Toponimo.from(payload, usuarioId);

        if (toponimoRepository.existsByNombreIgnoreCase(payload.nombre())) {
            throw new ConflictException(String.format("Ya existe un topónimo con el nombre: %s", payload.nombre()));
        }

        return toponimoRepository.save(toponimo).getId();
    }

    public Toponimo findById(Long toponimoId) throws RecordNotFoundException {

        log.info("Recuperando datos del topónimo con identificador: {}", toponimoId);
        return toponimoRepository.findById(toponimoId).orElseThrow(() -> new RecordNotFoundException(
                String.format("El topónimo solicitado con el id: %s no se encuentra registrado.", toponimoId)));
    }

    public void update(Long toponimoId, @Valid UpdateToponimoDto payload, Long usuarioId)
            throws RecordNotFoundException {

        log.info("Iniciando actualización del topónimo con ID: {}. Datos recibidos: {}", toponimoId, payload);
        Toponimo toponimo = findById(toponimoId);

        toponimo.updateWith(payload, usuarioId);

        Long cantidadToponimosDuplicados = toponimoRepository.countByNombreIgnoreCaseAndIdNot(toponimo.getNombre(),
                toponimoId);
        if (cantidadToponimosDuplicados > 0) {
            log.info("Validación fallida: Ya existe otro topónimo con el mismo nombre: {}", toponimo.getNombre());
            throw new ConflictException(
                    String.format("No se puede actualizar: ya existe este nombre registrado: %s",
                            toponimo.getNombre()));
        }

        toponimoRepository.save(toponimo);
    }

}
