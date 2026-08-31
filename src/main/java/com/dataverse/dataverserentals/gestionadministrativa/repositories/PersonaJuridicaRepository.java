package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dataverse.dataverserentals.gestionadministrativa.entities.PersonaJuridica;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;

public interface PersonaJuridicaRepository extends JpaRepository<PersonaJuridica, Long>, JpaSpecificationExecutor<PersonaJuridica> {

    Long countByChecksum(String checksum);

    Long countByTipoDocumentoCodeAndNumeroDocumento(TipoDocumento tipoDocumentoCode, String numeroDocumento);

    Long countByChecksumAndPersonaJuridicaIdNotIn(String checksum, List<Long> ids);

    Long countByTipoDocumentoCodeAndNumeroDocumentoAndPersonaJuridicaIdNotIn(TipoDocumento tipoDocumentoCode, String numeroDocumento, List<Long> ids);

}