package com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedidorSpecifications {

    public static Specification<Medidor> tipoMedidorOrNumeroSerieContains(String q) {
        if (q == null || q.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }

        String qUpper = q.toUpperCase();

        // Buscamos los enums que coincidan con el texto (nombre del enum o su descripción friendly)
        List<TipoMedidor> matchingEnums = Arrays.stream(TipoMedidor.values())
                .filter(t -> t.name().contains(qUpper) || t.intoString().toUpperCase().contains(qUpper))
                .toList();

        return (root, query, cb) -> {
            Predicate enumPredicate = matchingEnums.isEmpty()
                    ? cb.disjunction()
                    : root.get("tipoMedidorCode").in(matchingEnums);

            return cb.or(
                    enumPredicate,
                    cb.like(cb.upper(root.get("numeroSerie")), "%" + qUpper + "%"));
        };
    }

    public static Specification<Medidor> byUnidadId(Long unidadId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("unidadId"), unidadId);
        };
    }

    public static Specification<Medidor> byUnidadIdOrTipoMedidorOrNumeroSerieContains(Long unidadId, String q) {
        return byUnidadId(unidadId).and(tipoMedidorOrNumeroSerieContains(q));
    }

}
