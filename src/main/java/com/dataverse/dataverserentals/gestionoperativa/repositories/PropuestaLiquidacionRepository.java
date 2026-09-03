package com.dataverse.dataverserentals.gestionoperativa.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.enums.EstadoPropuesta;

public interface PropuestaLiquidacionRepository extends JpaRepository<PropuestaLiquidacion, Long> {
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
