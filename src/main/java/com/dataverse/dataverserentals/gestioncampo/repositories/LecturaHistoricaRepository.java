package com.dataverse.dataverserentals.gestioncampo.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;

public interface LecturaHistoricaRepository
        extends JpaRepository<LecturaHistorica, Long>, JpaSpecificationExecutor<LecturaHistorica> {

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
