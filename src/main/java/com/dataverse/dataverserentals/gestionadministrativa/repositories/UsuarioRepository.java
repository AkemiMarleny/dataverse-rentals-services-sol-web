package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    boolean existsByNombreUsuario(String nombreUsuario);

    java.util.List<Usuario> findAllByMfaSecretIsNotNullAndMfaEnabledIsTrue();
}
