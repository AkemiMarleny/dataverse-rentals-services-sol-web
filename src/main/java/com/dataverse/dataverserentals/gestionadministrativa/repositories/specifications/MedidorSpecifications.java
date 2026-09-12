package com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedidorSpecifications {

    public static Specification<Medidor> tipoMedidorOrNumeroSerieContains(String q) {
        String queryWillcard = "%" + q + "%";

        return (root, query, where) -> {
            return where.or(
                    where.like(root.get("tipoMedidorCode"), queryWillcard),
                    where.like(root.get("numeroSerie"), queryWillcard));
        };
    }

    public static Specification<Medidor> byUnidadId(Long unidadId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("unidadId"), unidadId);
        };
    }

    public static Specification<Medidor> byUnidadOrTipoMedidorOrNumeroSerieContains(Long unidadId, String q) {
        return byUnidadId(unidadId).and(tipoMedidorOrNumeroSerieContains(q));
    }

}
