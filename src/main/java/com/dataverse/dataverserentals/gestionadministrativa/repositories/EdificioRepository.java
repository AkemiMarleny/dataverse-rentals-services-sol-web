package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;

public interface EdificioRepository extends JpaRepository<Edificio, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    Page<Edificio> findByNombreContainingIgnoreCase(Pageable pageable, String q);

    Long countByNombreIgnoreCaseAndIdNot(String nombre, Long id);

}
