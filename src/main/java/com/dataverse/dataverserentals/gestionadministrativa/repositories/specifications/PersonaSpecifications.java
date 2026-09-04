package com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonaSpecifications {

    public static Specification<Persona> nombresOrApellidosOrNumeroDocumentoContains(String q,
            TipoPersona tipoPersonaCode) {
        String queryWillcard = "%" + q + "%";

        return (root, query, where) -> {

            return where.and(where.equal(root.get("tipoPersonaCode"), tipoPersonaCode), where.or(
                    where.like(root.get("nombres"), queryWillcard),
                    where.like(root.get("apellidos"), queryWillcard),
                    where.like(root.get("numeroDocumento"), queryWillcard)));

        };
    }

    public static Specification<Persona> nombresOrApellidosOrNumeroDocumentoOrNombreComercialContains(String q,
            TipoPersona tipoPersonaCode) {
        String queryWillcard = "%" + q + "%";

        return (root, query, where) -> {

            return where.and(where.equal(root.get("tipoPersonaCode"), tipoPersonaCode),
                    where.or(
                            where.like(root.get("nombres"), queryWillcard),
                            where.like(root.get("apellidos"), queryWillcard),
                            where.like(root.get("numeroDocumento"), queryWillcard),
                            where.like(root.get("nombreComercial"), queryWillcard)));

        };
    }

    public static Specification<Persona> razonSocialOrNumeroDocumentoContains(String q, TipoPersona tipoPersonaCode) {
        String queryWillcard = "%" + q + "%";

        return (root, query, where) -> {

            return where.and(where.equal(root.get("tipoPersonaCode"), tipoPersonaCode),
                    where.or(
                            where.like(root.get("razonSocial"), queryWillcard),
                            where.like(root.get("numeroDocumento"), queryWillcard)));

        };
    }

}
