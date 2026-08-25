package com.dataverse.dataverserentals.administrations.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dataverse.dataverserentals.administrations.entities.Medidor;
import com.dataverse.dataverserentals.administrations.enums.TipoMedidor;

public interface MedidorRepository extends JpaRepository<Medidor, Long>, JpaSpecificationExecutor<Medidor> {

    boolean existsByUnidadIdAndTipoMedidorCode(Long unidadId, TipoMedidor tipoMedidorCode);

    boolean existsByNumeroSerie(String numeroSerie);

    Optional<Medidor> findByUnidadIdAndId(Long unidadId, Long id);

}
