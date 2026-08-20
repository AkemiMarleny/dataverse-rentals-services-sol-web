package com.dataverse.dataverserentals.administrations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.administrations.entities.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

}
