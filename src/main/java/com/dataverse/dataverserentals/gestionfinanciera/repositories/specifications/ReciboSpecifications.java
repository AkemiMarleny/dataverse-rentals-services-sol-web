package com.dataverse.dataverserentals.gestionfinanciera.repositories.specifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionfinanciera.entities.Recibo;
import com.dataverse.dataverserentals.gestionfinanciera.enums.EstadoRecibo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReciboSpecifications {

    public static Specification<Recibo> hasEstadoCode(Optional<EstadoRecibo> estadoCodeOpt) {
        return (root, query, criteriaBuilder) -> {
            if (estadoCodeOpt.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("estadoCode"), estadoCodeOpt.get());
        };
    }

    public static Specification<Recibo> hasEdificioId(Optional<Long> edificioIdOpt) {
        return (root, query, criteriaBuilder) -> {
            if (edificioIdOpt.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("contrato").get("unidad").get("edificioId"), edificioIdOpt.get());
        };
    }

    public static Specification<Recibo> hasFechaDesde(Optional<LocalDateTime> fechaDesdeOpt) {
        return (root, query, criteriaBuilder) -> {
            if (fechaDesdeOpt.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("fechaEmision"), fechaDesdeOpt.get());
        };
    }

    public static Specification<Recibo> hasFechaHasta(Optional<LocalDateTime> fechaHastaOpt) {
        return (root, query, criteriaBuilder) -> {
            if (fechaHastaOpt.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("fechaEmision"), fechaHastaOpt.get());
        };
    }

    public static Specification<Recibo> hasContratoIds(List<Long> contratoIds) {
        return (root, query, criteriaBuilder) -> {
            if (contratoIds == null || contratoIds.isEmpty()) {
                return criteriaBuilder.disjunction();
            }
            return root.get("contratoId").in(contratoIds);
        };
    }

    public static Specification<Recibo> queryableBy(
            Optional<EstadoRecibo> estadoCode,
            Optional<LocalDateTime> fechaDesde,
            Optional<LocalDateTime> fechaHasta) {
        return hasEstadoCode(estadoCode)
                .and(hasFechaDesde(fechaDesde))
                .and(hasFechaHasta(fechaHasta));
    }
}