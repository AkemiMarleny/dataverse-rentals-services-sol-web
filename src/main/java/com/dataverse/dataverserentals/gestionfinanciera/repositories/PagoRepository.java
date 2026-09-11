package com.dataverse.dataverserentals.gestionfinanciera.repositories;

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

import com.dataverse.dataverserentals.gestionfinanciera.entities.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long>, JpaSpecificationExecutor<Pago> {

    @Query("SELECT p FROM Pago p LEFT JOIN FETCH p.validador WHERE p.reciboId = :reciboId")
    Page<Pago> findAllByReciboId(@Param("reciboId") Long reciboId, Pageable pageable);

    List<Pago> findByReciboId(Long reciboId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pago p WHERE p.id = :id")
    Optional<Pago> findByIdForUpdate(@Param("id") Long id);
}
