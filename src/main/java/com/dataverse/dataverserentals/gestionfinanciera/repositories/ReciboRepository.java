package com.dataverse.dataverserentals.gestionfinanciera.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dataverse.dataverserentals.gestionfinanciera.entities.Recibo;
import com.dataverse.dataverserentals.gestionfinanciera.enums.EstadoRecibo;
import com.dataverse.dataverserentals.gestionfinanciera.repositories.specifications.ReciboSpecifications;

public interface ReciboRepository extends JpaRepository<Recibo, Long>, JpaSpecificationExecutor<Recibo> {

    default Page<Recibo> findAllRecibos(
            Optional<EstadoRecibo> estadoCode,
            Optional<LocalDateTime> fechaDesde,
            Optional<LocalDateTime> fechaHasta,
            Pageable pageable) {
        return findAll(
                ReciboSpecifications.queryableBy(estadoCode, fechaDesde, fechaHasta),
                pageable);
    }

    default Page<Recibo> findAllInquilinoRecibos(
            Optional<EstadoRecibo> estadoCode,
            Optional<LocalDateTime> fechaDesde,
            Optional<LocalDateTime> fechaHasta,
            List<Long> contratoIds,
            Pageable pageable) {
        return findAll(
                ReciboSpecifications.hasContratoIds(contratoIds)
                        .and(ReciboSpecifications.queryableBy(estadoCode, fechaDesde, fechaHasta)),
                pageable);
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Recibo r WHERE r.id = :id")
    Optional<Recibo> findByIdForUpdate(@Param("id") Long id);
}