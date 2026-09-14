package com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonaSpecifications {

    public static Specification<Persona> byTipoPersona(
            String q,
            Optional<TipoPersona> tipoPersona) {
        if (tipoPersona.isEmpty()) {
            return PersonaSpecifications.numeroDocumentoContains(q);
        }

        return switch (tipoPersona.get()) {
            case NATURAL_SIN_NEGOCIO ->
                PersonaSpecifications.nombresOrApellidosOrNumeroDocumentoContains(q, tipoPersona.get());
            case NATURAL_CON_NEGOCIO ->
                PersonaSpecifications.nombresOrApellidosOrNumeroDocumentoOrNombreComercialContains(q,
                        tipoPersona.get());
            case JURIDICA ->
                PersonaSpecifications.razonSocialOrNumeroDocumentoContains(q, tipoPersona.get());
        };
    }

    public static Specification<Persona> nombresOrApellidosOrNumeroDocumentoContains(String q,
            TipoPersona tipoPersonaCode) {
        if (q == null || q.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }

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
        if (q == null || q.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }

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
        if (q == null || q.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }

        String queryWillcard = "%" + q + "%";

        return (root, query, where) -> {

            return where.and(where.equal(root.get("tipoPersonaCode"), tipoPersonaCode),
                    where.or(
                            where.like(root.get("razonSocial"), queryWillcard),
                            where.like(root.get("numeroDocumento"), queryWillcard)));

        };
    }

    public static Specification<Persona> numeroDocumentoContains(String q) {
        if (q == null || q.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }

        String queryWillcard = "%" + q + "%";

        return (root, query, where) -> {

            return where.or(where.like(root.get("numeroDocumento"), queryWillcard));
        };
    }

}
