package com.dataverse.dataverserentals.gestioncampo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;

public interface LecturaHistoricaRepository extends JpaRepository<LecturaHistorica, Long> {

    Page<LecturaHistorica> findByMedidorId(Long medidorId, Pageable pageable);

}
