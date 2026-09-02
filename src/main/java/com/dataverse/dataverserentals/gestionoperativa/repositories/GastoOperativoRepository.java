package com.dataverse.dataverserentals.gestionoperativa.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoGasto;

public interface GastoOperativoRepository extends JpaRepository<GastoOperativo, Long> {
    @Query("""
            SELECT SUM(go.monto)
            FROM GastoOperativo go
            WHERE go.edificioId = :edificioId
            AND go.tipoGastoCode in (:gastoTipos)
            AND go.fechaGasto >= :periodoInicio
            AND go.fechaGasto <= :periodoFin
            """)
    Optional<BigDecimal> sumMontoByEdificioAndTipoGastoAndPeriodo(
            Long edificioId,
            List<TipoGasto> gastoTipos,
            LocalDateTime periodoInicio,
            LocalDateTime periodoFin);
}
