package com.dataverse.dataverserentals.gestionoperativa.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.gestionoperativa.entities.DetallePropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoConceptoPropuesta;

public interface DetallePropuestaLiquidacionRepository extends JpaRepository<DetallePropuestaLiquidacion, Long> {

    Optional<DetallePropuestaLiquidacion> findFirstByPropuestaLiquidacionIdAndTipoConceptoPropuesta(
            Long propuestaLiquidacionId,
            TipoConceptoPropuesta tipoConceptoPropuesta);

    Page<DetallePropuestaLiquidacion> findByPropuestaLiquidacionId(Long propuestaLiquidacionId, Pageable pageable);

    void deleteByPropuestaLiquidacionId(Long propuestaLiquidacionId);
}
