package com.dataverse.dataverserentals.administrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dataverse.dataverserentals.administrations.entities.Persona;
import com.dataverse.dataverserentals.administrations.enums.TipoDocumento;

public interface PersonaRepository extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {

    boolean existsByChecksum(String checksum);

    boolean existsByTipoDocumentoCodeAndNumeroDocumento(TipoDocumento tipoDocumentoCode, String numeroDocumento);

    boolean existsByNumeroCelular(String numeroCelular);

    boolean existsByCorreo(String correo);

    Long countByChecksumAndIdNot(String checksum, Long id);

    Long countByTipoDocumentoCodeAndNumeroDocumentoAndIdNot(TipoDocumento tipoDocumentoCode, String numeroDocumento,
            Long id);

    Long countByNumeroCelularAndIdNot(String numeroCelular, Long id);

    Long countByCorreoAndIdNot(String correo, Long id);

}
