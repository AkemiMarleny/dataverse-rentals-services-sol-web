package com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;

import jakarta.persistence.criteria.Predicate;

public class UnidadSpecifications {
    public static Specification<Unidad> numeroPisoOrNumeroPuertaContains(String q) {
        // Si el parámetro está vacío, devolvemos una condición que siempre es verdadera
        if (q == null || q.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }

        String queryWildcard = "%" + q + "%";

        return (root, query, cb) -> {
            // CAST para el Integer numeroPiso
            Predicate pisoPredicate = cb.like(
                    root.get("numeroPiso").as(String.class),
                    queryWildcard);

            // Comparación directa para el String numeroPuerta
            Predicate puertaPredicate = cb.like(
                    root.get("numeroPuerta"),
                    queryWildcard);

            return cb.or(pisoPredicate, puertaPredicate);
        };
    }

    public static Specification<Unidad> byEdificioId(Long edificioId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("edificioId"), edificioId);
        };
    }

    public static Specification<Unidad> byEdificioIdOrPisoOrPuertaContains(Long edificioId, String q) {
        return byEdificioId(edificioId).and(numeroPisoOrNumeroPuertaContains(q));
    }
}
