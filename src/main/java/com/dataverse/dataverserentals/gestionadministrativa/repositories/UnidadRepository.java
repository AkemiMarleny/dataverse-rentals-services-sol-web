package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;

public interface UnidadRepository extends JpaRepository<Unidad, Long> {

    boolean existsByEdificioIdAndNumeroPuerta(Long edificioId, String numeroPuerta);

    @Query("SELECT u FROM Unidad u LEFT JOIN u.edificio e " +
            "WHERE CAST(u.numeroPiso AS string) LIKE %:q% " +
            "OR u.numeroPuerta LIKE %:q% " +
            "OR e.nombre LIKE %:q%")
    Page<Unidad> nombreEdificioOrNumeroPisoOrNumeroPuertaContains(@Param("q") String q, Pageable pageable);

    Long countByEdificioIdAndNumeroPuertaAndIdNot(Long edificioId, String numeroPuerta, Long id);

    @Query("""
            SELECT SUM(u.areaMetrosCuadrados)
            FROM Unidad u
            WHERE u.edificioId = :edificioId
            """)
    Optional<BigDecimal> sumAreaMetrosCuadradosByEdificio(Long edificioId);
}
