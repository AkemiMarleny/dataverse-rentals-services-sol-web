package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dataverse.dataverserentals.gestionadministrativa.entities.VContrato;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoContrato;

public interface VContratoRepository extends JpaRepository<VContrato, Long> {
    @Query("""
            SELECT c
            FROM VContrato c
            WHERE
                c.estado = :estado
                AND (
                    LOWER(c.persona.nombres) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(c.persona.apellidos) LIKE LOWER(CONCAT('%', :q, '%'))
                )
            """)
    Page<VContrato> findByEstadoAndPersonaNombresOrPersonaApellidosContaining(
            @Param("estado") EstadoContrato estado,
            @Param("q") String q,
            Pageable pageable);

    @Query("""
            SELECT c
            FROM VContrato c
            WHERE c.personaId = :personaId
            AND c.estado = :estado
            """)
    Page<VContrato> findByPersonaIdAndEstado(
            @Param("personaId") Long personaId,
            @Param("estado") EstadoContrato estado,
            Pageable pageable);

}
