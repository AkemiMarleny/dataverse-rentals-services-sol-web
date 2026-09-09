package com.dataverse.dataverserentals.gestionfinanciera.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.gestionfinanciera.entities.DetalleRecibo;

public interface DetalleReciboRepository extends JpaRepository<DetalleRecibo, Long> {

    Page<DetalleRecibo> findByReciboId(Long reciboId, Pageable pageable);
}
