package com.dataverse.dataverserentals.gestionoperativa.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;

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

    public static Specification<GastoOperativo> byCategoriaGastoId(Long categoriaGastoId) {
        if (categoriaGastoId == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.equal(root.get("categoriaGastoId"), categoriaGastoId);
    }

    public static Specification<GastoOperativo> filterBy(Long edificioId, Long categoriaGastoId) {
        return byEdificioId(edificioId).and(byCategoriaGastoId(categoriaGastoId));
    }
}