package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;

public interface MedidorRepository extends JpaRepository<Medidor, Long>, JpaSpecificationExecutor<Medidor> {

    boolean existsByUnidadIdAndTipoMedidorCode(Long unidadId, TipoMedidor tipoMedidorCode);

    boolean existsByNumeroSerie(String numeroSerie);

    Optional<Medidor> findByUnidadIdAndId(Long unidadId, Long id);

    Long countByUnidadIdAndTipoMedidorCodeAndIdNot(Long unidadId, TipoMedidor tipoMedidorCode, Long id);

    Long countByNumeroSerieAndIdNot(String numeroSerie, Long id);

    @Query("""
            SELECT m
            FROM Medidor m
            LEFT JOIN m.unidad u
            WHERE u.edificioId = :edificioId
            AND m.tipoMedidorCode = :tipoMedidor
            """)
    Optional<Medidor> findByEdificioAndTipoMedidor(
            Long edificioId,
            TipoMedidor tipoMedidor);

    @Query("""
            SELECT m
            FROM Medidor m
            LEFT JOIN m.unidad u
            WHERE u.edificioId = :edificioId
            AND m.tipoMedidorCode = :tipoMedidor
            """)
    List<Medidor> findAllByEdificioAndTipoMedidor(
            Long edificioId,
            TipoMedidor tipoMedidor);

    @Query("""
            SELECT m
            FROM Medidor m
            WHERE m.unidadId = :unidadId
            AND m.tipoMedidorCode = :tipoMedidor
            """)
    Optional<Medidor> findByUnidadAndTipoMedidor(
            Long unidadId,
            TipoMedidor tipoMedidor);

}
