package com.dataverse.dataverserentals.gestionoperativa.repositories.specifications;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropuestaSpecifications {
    public static Specification<PropuestaLiquidacion> hasEdificioId(Optional<Long> edificioIdOpt) {
        return (root, query, criteriaBuilder) -> {
            if (edificioIdOpt.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("edificioId"), edificioIdOpt.get());
        };
    }

    public static Specification<PropuestaLiquidacion> hasFechaDesde(Optional<LocalDateTime> fechaDesdeOpt) {
        return (root, query, criteriaBuilder) -> {
            if (fechaDesdeOpt.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get("fechaCalculo"), fechaDesdeOpt.get());
        };
    }

    public static Specification<PropuestaLiquidacion> hasFechaHasta(Optional<LocalDateTime> fechaHastaOpt) {
        return (root, query, criteriaBuilder) -> {
            if (fechaHastaOpt.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("fechaCalculo"), fechaHastaOpt.get());
        };
    }

    public static Specification<PropuestaLiquidacion> queryableBy(
            Optional<Long> edificioId,
            Optional<LocalDateTime> fechaDesde,
            Optional<LocalDateTime> fechaHasta) {
        return hasEdificioId(edificioId)
                .and(PropuestaSpecifications.hasFechaDesde(fechaDesde))
                .and(PropuestaSpecifications.hasFechaHasta(fechaHasta));
    }
}
