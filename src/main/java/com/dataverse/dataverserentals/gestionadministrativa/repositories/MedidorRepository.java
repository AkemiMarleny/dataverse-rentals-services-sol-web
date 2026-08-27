package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;

public interface MedidorRepository extends JpaRepository<Medidor, Long>, JpaSpecificationExecutor<Medidor> {

    boolean existsByUnidadIdAndTipoMedidorCode(Long unidadId, TipoMedidor tipoMedidorCode);

    boolean existsByNumeroSerie(String numeroSerie);

    Optional<Medidor> findByUnidadIdAndId(Long unidadId, Long id);

    Long countByUnidadIdAndTipoMedidorCodeAndIdNot(Long unidadId, TipoMedidor tipoMedidorCode, Long id);

    Long countByNumeroSerieAndIdNot(String numeroSerie, Long id);

}
