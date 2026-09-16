package com.dataverse.dataverserentals.gestionadministrativa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByNombreUsuarioIgnoreCase(String nombreUsuario);

    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);

    boolean existsByNombreUsuarioIgnoreCaseAndIdNot(String nombreUsuario, Long id);

    java.util.List<Usuario> findAllByMfaSecretIsNotNullAndMfaEnabledIsTrue();
}
