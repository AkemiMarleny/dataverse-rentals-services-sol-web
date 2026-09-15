package com.dataverse.dataverserentals.gestionoperativa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dataverse.dataverserentals.gestionoperativa.entities.CategoriaGasto;

public interface CategoriaGastoRepository extends JpaRepository<CategoriaGasto, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

}