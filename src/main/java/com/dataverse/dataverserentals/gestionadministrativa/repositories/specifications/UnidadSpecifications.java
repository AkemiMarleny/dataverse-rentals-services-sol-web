package com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;

public class UnidadSpecifications {
    public static Specification<Unidad> numeroPuertaContains(String q) {
        if (q == null || q.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }
        String queryWildcard = "%" + q + "%";
        return (root, query, cb) -> cb.like(root.get("numeroPuerta"), queryWildcard);
    }

    public static Specification<Unidad> byEdificioId(Long edificioId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("edificioId"), edificioId);
        };
    }

    public static Specification<Unidad> byEdificioIdOrNumeroPuertaContains(Long edificioId, String q) {
        return byEdificioId(edificioId).and(numeroPuertaContains(q));
    }
}
