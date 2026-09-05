package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    @Query("""
            SELECT c
            FROM Contrato c
            WHERE c.fechaCierre is NULL
            AND c.fechaInicio <= :fechaInicio
                """)
    List<Contrato> findAllActivosNoVencidos(
            LocalDateTime fechaInicio);

    @Query("""
            SELECT c.id
            FROM Contrato c
            WHERE c.personaId = :personaId
            """)
    List<Long> findContratoIdsByPersonaId(Long personaId);
}
