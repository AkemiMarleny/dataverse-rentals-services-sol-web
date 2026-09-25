package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Toponimo;

public interface ToponimoRepository extends JpaRepository<Toponimo, Long> {

    Page<Toponimo> findByNombreIgnoreCaseContaining(Pageable pageable, String q);

    boolean existsByNombreIgnoreCase(String nombre);

    Long countByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
