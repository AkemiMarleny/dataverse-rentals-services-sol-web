package com.dataverse.dataverserentals.gestionfinanciera.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.ContratoRepository;
import com.dataverse.dataverserentals.gestionfinanciera.entities.Pago;
import com.dataverse.dataverserentals.gestionfinanciera.entities.Recibo;
import com.dataverse.dataverserentals.gestionfinanciera.repositories.PagoRepository;
import com.dataverse.dataverserentals.gestionfinanciera.repositories.ReciboRepository;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReciboRepository reciboRepository;
    private final ContratoRepository contratoRepository;

    public Page<Pago> findPagosByReciboId(Long reciboId, UsuarioPrincipal usuarioPrincipal, Pageable pageable)
            throws AccessDeniedException {
        if (RolUsuario.ADMINISTRADOR.equals(usuarioPrincipal.usuarioRol())) {
            return pagoRepository.findAllByReciboId(reciboId, pageable);
        }

        if (RolUsuario.INQUILINO.equals(usuarioPrincipal.usuarioRol())) {
            Long personaId = usuarioPrincipal.personaId();
            List<Long> contratoIds = findContratoIdsByPersonaId(personaId);
            Recibo recibo = findReciboById(reciboId);

            if (!contratoIds.contains(recibo.getContratoId())) {
                throw new AccessDeniedException(usuarioPrincipal);
            }

            return pagoRepository.findAllByReciboId(reciboId, pageable);
        }

        throw new AccessDeniedException(usuarioPrincipal);
    }

    public Recibo findReciboById(Long reciboId) {
        return reciboRepository.findById(reciboId)
                .orElseThrow(() -> new IllegalArgumentException("Recibo no encontrado con id: " + reciboId));
    }

    public List<Long> findContratoIdsByPersonaId(Long personaId) {
        return contratoRepository.findContratoIdsByPersonaId(personaId);
    }
}
