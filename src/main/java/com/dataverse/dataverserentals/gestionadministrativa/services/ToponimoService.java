package com.dataverse.dataverserentals.gestionadministrativa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Toponimo;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.ToponimoRepository;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

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
}
