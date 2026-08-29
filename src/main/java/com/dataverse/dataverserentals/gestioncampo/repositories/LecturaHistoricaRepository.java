package com.dataverse.dataverserentals.gestioncampo.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;

public interface LecturaHistoricaRepository extends JpaRepository<LecturaHistorica, Long> {

    Page<LecturaHistorica> findByMedidorId(Long medidorId, Pageable pageable);

    Page<LecturaHistorica> findByMedidorIdAndFechaLecturaBetween(
            Long medidorId, LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    Optional<LecturaHistorica> findByMedidorIdAndId(Long medidorId, Long id);

}
