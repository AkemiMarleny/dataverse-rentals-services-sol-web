package com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsuarioSpecifications {

    public static Specification<Usuario> bySearchTerm(String q) {
        if (q == null || q.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }

        String queryWillcard = "%" + q.toUpperCase() + "%";

        return (root, query, cb) -> cb.or(
                cb.like(cb.upper(root.get("nombreUsuario")), queryWillcard),
                cb.like(cb.upper(root.join("persona").get("nombres")), queryWillcard),
                cb.like(cb.upper(root.join("persona").get("apellidos")), queryWillcard),
                cb.like(cb.upper(root.join("persona").get("razonSocial")), queryWillcard));
    }
}
