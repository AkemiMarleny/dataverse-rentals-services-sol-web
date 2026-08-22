package com.dataverse.dataverserentals.administrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.administrations.entities.Edificio;

public interface EdificioRepository extends JpaRepository<Edificio, Long> {

    boolean existsByNombre(String nombre);

}
