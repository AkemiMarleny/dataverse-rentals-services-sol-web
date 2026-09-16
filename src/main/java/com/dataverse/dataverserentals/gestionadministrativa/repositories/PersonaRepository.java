package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;

public interface PersonaRepository extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {

    boolean existsByPersonaNaturalChecksum(String personaNaturalChecksum);

    boolean existsByPersonaNaturalNegocioChecksum(String personaNaturalNegocioChecksum);

    boolean existsByPersonaJuridicaChecksum(String personaJuridicaChecksum);

    boolean existsByTipoDocumentoCodeAndNumeroDocumentoIgnoreCase(TipoDocumento tipoDocumentoCode, String numeroDocumento);

    Optional<Persona> findByTipoDocumentoCodeAndNumeroDocumentoIgnoreCase(TipoDocumento tipoDocumentoCode,
            String numeroDocumento);

    boolean existsByNumeroCelular(String numeroCelular);

    boolean existsByCorreoIgnoreCase(String correo);

    Long countByPersonaNaturalChecksumAndIdNot(String personaNaturalChecksum, Long id);

    Long countByPersonaNaturalNegocioChecksumAndIdNot(String personaNaturalNegocioChecksum, Long id);

    Long countByPersonaJuridicaChecksumAndIdNot(String personaJuridicaChecksum, Long id);

    Long countByTipoDocumentoCodeAndNumeroDocumentoIgnoreCaseAndIdNot(TipoDocumento tipoDocumentoCode, String numeroDocumento,
            Long id);

    Long countByNumeroCelularAndIdNot(String numeroCelular, Long id);

    Long countByCorreoIgnoreCaseAndIdNot(String correo, Long id);

    boolean existsByNombreComercialIgnoreCase(String nombreComercial);

    Long countByNombreComercialIgnoreCaseAndIdNot(String nombreComercial, Long id);

    boolean existsByRazonSocialIgnoreCase(String razonSocial);

    Long countByRazonSocialIgnoreCaseAndIdNot(String razonSocial, Long id);
}