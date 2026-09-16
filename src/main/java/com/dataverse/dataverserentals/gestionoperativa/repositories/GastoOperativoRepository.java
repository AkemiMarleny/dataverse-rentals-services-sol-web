package com.dataverse.dataverserentals.gestionoperativa.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoGasto;

public interface GastoOperativoRepository
        extends JpaRepository<GastoOperativo, Long>, JpaSpecificationExecutor<GastoOperativo> {
    
    Optional<GastoOperativo> findByIdAndEdificioId(Long id, Long edificioId);

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

    @Query(value = "SELECT CAST(EXTRACT(MONTH FROM fecha_gasto) AS INTEGER) as mes, SUM(monto) as total " +
            "FROM gastos_operativos WHERE EXTRACT(YEAR FROM fecha_gasto) = :anio " +
            "GROUP BY EXTRACT(MONTH FROM fecha_gasto)", nativeQuery = true)
    List<Object[]> findEgresosAgrupadosByAnio(@Param("anio") int anio);
}
