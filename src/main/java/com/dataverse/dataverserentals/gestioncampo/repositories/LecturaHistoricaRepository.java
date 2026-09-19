package com.dataverse.dataverserentals.gestioncampo.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;

public interface LecturaHistoricaRepository extends JpaRepository<LecturaHistorica, Long> {

    Page<LecturaHistorica> findByMedidorId(Long medidorId, Pageable pageable);

    Page<LecturaHistorica> findByMedidorIdAndFechaLecturaBetween(
            Long medidorId, LocalDate desde, LocalDate hasta, Pageable pageable);

    Page<LecturaHistorica> findByFechaLecturaBetween(LocalDate desde, LocalDate hasta, Pageable pageable);

    Optional<LecturaHistorica> findByMedidorIdAndId(Long medidorId, Long id);

    Optional<LecturaHistorica> findFirstByMedidorIdAndFechaLecturaBetweenOrderByFechaLecturaAsc(
            Long medidorId,
            LocalDate fechaInicioPeriodo,
            LocalDate fechaFinPeriodo);

    Optional<LecturaHistorica> findFirstByMedidorIdAndFechaLecturaBetweenOrderByFechaLecturaDesc(
            Long medidorId,
            LocalDate fechaInicioPeriodo,
            LocalDate fechaFinPeriodo);

}
