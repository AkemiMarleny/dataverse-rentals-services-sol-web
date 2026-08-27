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

}
