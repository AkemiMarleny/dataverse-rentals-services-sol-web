package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;

public interface UnidadRepository extends JpaRepository<Unidad, Long>, JpaSpecificationExecutor<Unidad> {

    boolean existsByEdificioIdAndNumeroPuerta(Long edificioId, String numeroPuerta);

    Long countByEdificioIdAndNumeroPuertaAndIdNot(Long edificioId, String numeroPuerta, Long id);

    @Query("""
            SELECT SUM(u.areaMetrosCuadrados)
            FROM Unidad u
            WHERE u.edificioId = :edificioId
            """)
    Optional<BigDecimal> sumAreaMetrosCuadradosByEdificio(Long edificioId);
}
