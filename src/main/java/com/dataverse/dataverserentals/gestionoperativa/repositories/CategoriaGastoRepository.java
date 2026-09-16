package com.dataverse.dataverserentals.gestionoperativa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.gestionoperativa.entities.CategoriaGasto;

public interface CategoriaGastoRepository extends JpaRepository<CategoriaGasto, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    Page<CategoriaGasto> findByNombreContainingIgnoreCase(Pageable pageable, String q);

    Long countByNombreIgnoreCaseAndIdNot(String nombre, Long id);

}