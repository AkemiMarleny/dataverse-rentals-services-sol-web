package com.dataverse.dataverserentals.gestionfinanciera.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dataverse.dataverserentals.gestionfinanciera.entities.Pago;

import jakarta.persistence.LockModeType;

public interface PagoRepository extends JpaRepository<Pago, Long>, JpaSpecificationExecutor<Pago> {

    @Query("SELECT p FROM Pago p LEFT JOIN FETCH p.validador WHERE p.reciboId = :reciboId")
    Page<Pago> findAllByReciboId(@Param("reciboId") Long reciboId, Pageable pageable);

    List<Pago> findByReciboId(Long reciboId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pago p WHERE p.id = :id")
    Optional<Pago> findByIdForUpdate(@Param("id") Long id);

    @Query(value = "SELECT CAST(EXTRACT(MONTH FROM fecha_aprobacion) AS INTEGER) as mes, SUM(monto_pagado) as total " +
            "FROM pagos WHERE estado_codigo = 'APROBADO' AND EXTRACT(YEAR FROM fecha_aprobacion) = :anio " +
            "GROUP BY EXTRACT(MONTH FROM fecha_aprobacion)", nativeQuery = true)
    List<Object[]> findIngresosAgrupadosByAnio(@Param("anio") int anio);

    @Query(value = """
            SELECT
                CAST(EXTRACT(MONTH FROM p.fecha_aprobacion) AS INTEGER) as mes,
                COUNT(CASE WHEN EXTRACT(EPOCH FROM (p.fecha_aprobacion - r.fecha_vencimiento))/86400
                    BETWEEN 1 AND 30 THEN 1 END) as p1a30,
                COUNT(CASE WHEN EXTRACT(EPOCH FROM (p.fecha_aprobacion - r.fecha_vencimiento))/86400
                    BETWEEN 31 AND 60 THEN 1 END) as p31a60,
                COUNT(CASE WHEN EXTRACT(EPOCH FROM (p.fecha_aprobacion - r.fecha_vencimiento))/86400
                    > 60 THEN 1 END) as p60
            FROM pagos p
            INNER JOIN recibos r ON p.recibo_id = r.id
            WHERE p.estado_codigo = 'APROBADO'
            AND EXTRACT(YEAR FROM p.fecha_aprobacion) = :anio
            AND p.fecha_aprobacion > r.fecha_vencimiento
            GROUP BY EXTRACT(MONTH FROM p.fecha_aprobacion)
            ORDER BY mes
            """, nativeQuery = true)
    List<Object[]> findPagosMorososAgrupadosByAnio(@Param("anio") int anio);
}
