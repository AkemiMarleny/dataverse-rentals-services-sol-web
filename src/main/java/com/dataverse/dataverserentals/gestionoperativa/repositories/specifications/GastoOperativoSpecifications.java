package com.dataverse.dataverserentals.gestionoperativa.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionoperativa.entities.CategoriaGasto;
import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;

import jakarta.persistence.criteria.Join;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GastoOperativoSpecifications {

    public static Specification<GastoOperativo> byEdificioId(Long edificioId) {
        if (edificioId == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.equal(root.get("edificioId"), edificioId);
    }

    public static Specification<GastoOperativo> byCategoriaGastoNombre(String categoriaGastoNombre) {
        if (categoriaGastoNombre == null || categoriaGastoNombre.trim().isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }

        String queryWillcard = "%" + categoriaGastoNombre.toLowerCase() + "%";

        return (root, query, cb) -> {
            Join<CategoriaGasto, GastoOperativo> categoriaGastoOperativo = root.join("categoriaGasto");
            return cb.like(cb.lower(categoriaGastoOperativo.get("nombre")), queryWillcard);
        };

    }

    public static Specification<GastoOperativo> filterBy(Long edificioId, String categoriaGastoNombre) {

        return byEdificioId(edificioId).and(byCategoriaGastoNombre(categoriaGastoNombre));
    }
}