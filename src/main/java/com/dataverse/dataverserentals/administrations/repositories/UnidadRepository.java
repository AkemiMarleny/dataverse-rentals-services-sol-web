package com.dataverse.dataverserentals.administrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.administrations.entities.Unidad;

public interface UnidadRepository extends JpaRepository<Unidad, Long> {

    boolean existsByEdificioIdAndNumeroPisoAndNumeroPuerta(Long edificioId, Integer numeroPiso, String numeroPuerta);

}
