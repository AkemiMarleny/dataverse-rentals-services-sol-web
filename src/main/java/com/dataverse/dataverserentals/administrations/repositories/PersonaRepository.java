package com.dataverse.dataverserentals.administrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dataverse.dataverserentals.administrations.entities.Persona;
import com.dataverse.dataverserentals.administrations.enums.TipoDocumento;

public interface PersonaRepository extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {

    boolean existsByTipoDocumentoCodeAndNumeroDocumento(TipoDocumento tipoDocumentoCode, String numeroDocumento);

    Long countByTipoDocumentoCodeAndNumeroDocumentoAndIdNot(TipoDocumento tipoDocumentoCode, String numeroDocumento,
            Long id);

}
