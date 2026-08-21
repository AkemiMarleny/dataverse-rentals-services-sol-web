package com.dataverse.dataverserentals.administrations.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.administrations.entities.Persona;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonaSpecifications {

    public static Specification<Persona> nombresOrApellidosOrNumeroDocumentoContains(String q) {
        String queryWillcard = "%" + q + "%";

        return (root, query, where) -> {
            return where.or(
                    where.like(root.get("nombres"), queryWillcard),
                    where.like(root.get("apellidos"), queryWillcard),
                    where.like(root.get("numeroDocumento"), queryWillcard));
        };
    }

}
