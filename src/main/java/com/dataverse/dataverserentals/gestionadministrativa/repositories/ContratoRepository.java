package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT c
            FROM Contrato c
            WHERE LOWER(c.persona.nombres) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(c.persona.apellidos) LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    Page<Contrato> findByPersonaNombresOrPersonaApellidosContaining(
            @Param("q") String q,
            Pageable pageable);
}
