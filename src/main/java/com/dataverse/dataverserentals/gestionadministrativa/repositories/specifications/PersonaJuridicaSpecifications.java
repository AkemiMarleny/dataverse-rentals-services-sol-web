package com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionadministrativa.entities.PersonaJuridica;

public class PersonaJuridicaSpecifications {

    public static Specification<PersonaJuridica> razonSocialOrNumeroDocumentoContains(String searchText) {
        return (root, query, cb) -> {
            if (searchText == null || searchText.isBlank()) {
                return null;
            }
            String lowerSearch = "%" + searchText.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("razonSocial")), lowerSearch),
                    cb.like(cb.lower(root.get("numeroDocumento")), lowerSearch)
            );
        };
    }
}