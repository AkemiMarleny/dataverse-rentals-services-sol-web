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
            WHERE c.personaId = :personaId
            """)
    Page<Contrato> findByPersonaId(@Param("personaId") Long personaId, Pageable pageable);

    @Query("""
            SELECT c
            FROM Contrato c
            WHERE c.personaId = :personaId
            AND (LOWER(c.persona.nombres) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(c.persona.apellidos) LIKE LOWER(CONCAT('%', :q, '%')))
            """)
    Page<Contrato> findByPersonaIdAndPersonaNombresOrPersonaApellidosContaining(
            @Param("personaId") Long personaId,
            @Param("q") String q,
            Pageable pageable);

    @Query("""
            SELECT c
            FROM Contrato c
            WHERE LOWER(c.persona.nombres) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(c.persona.apellidos) LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    Page<Contrato> findByPersonaNombresOrPersonaApellidosContaining(
            @Param("q") String q,
            Pageable pageable);

    @Query("""
            SELECT COUNT(DISTINCT c.unidadId)
            FROM Contrato c
            WHERE c.fechaInicio <= :ultimoDiaMes
            AND (
                (c.fechaCierre IS NULL AND c.fechaFin >= :primeroDiaMes)
                OR
                (c.fechaCierre IS NOT NULL AND c.fechaCierre >= :primeroDiaMes)
            )
            """)
    Long countUnidadesOcupadasByMes(
            @Param("primeroDiaMes") LocalDateTime primeroDiaMes,
            @Param("ultimoDiaMes") LocalDateTime ultimoDiaMes);
}
