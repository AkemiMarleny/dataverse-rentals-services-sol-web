package com.dataverse.dataverserentals.gestionoperativa.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.enums.EstadoPropuesta;
import com.dataverse.dataverserentals.gestionoperativa.repositories.specifications.PropuestaSpecifications;

public interface PropuestaLiquidacionRepository
        extends JpaRepository<PropuestaLiquidacion, Long>, JpaSpecificationExecutor<PropuestaLiquidacion> {

    default Page<PropuestaLiquidacion> findAllWithFilters(
            Optional<Long> edificioId,
            Optional<LocalDateTime> fechaDesde,
            Optional<LocalDateTime> fechaHasta,
            Pageable pageable) {
        return findAll(
                PropuestaSpecifications.queryableBy(edificioId, fechaDesde, fechaHasta),
                pageable);
    }

    @Query("""
            SELECT pl
            FROM PropuestaLiquidacion pl
            WHERE pl.edificioId = :edificioId
            AND pl.estadoCode = :estadoPropuesta
            AND pl.fechaCalculo >= :periodoInicio
            AND pl.fechaCalculo <= :periodoFin
            ORDER BY pl.id DESC
                """)
    Optional<PropuestaLiquidacion> findFirstByEdificioAndEstadoAndPeriodo(
            Long edificioId,
            EstadoPropuesta estadoPropuesta,
            LocalDateTime periodoInicio,
            LocalDateTime periodoFin);
}
