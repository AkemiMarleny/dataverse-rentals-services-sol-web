package com.dataverse.dataverserentals.administrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.administrations.entities.Medidor;
import com.dataverse.dataverserentals.administrations.enums.TipoMedidor;

public interface MedidorRepository extends JpaRepository<Medidor, Long> {

    boolean existsByUnidadIdAndTipoMedidor(Long unidadId, TipoMedidor tipoMedidor);

    boolean existsByNumeroSerie(String numeroSerie);

}
