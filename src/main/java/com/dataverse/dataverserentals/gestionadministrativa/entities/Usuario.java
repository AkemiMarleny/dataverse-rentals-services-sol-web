package com.dataverse.dataverserentals.gestionadministrativa.entities;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateUsuarioDto;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios", indexes = {
        @Index(name = "idx_usuarios_usuario_creacion", columnList = "usuarioCreacionId"),
        @Index(name = "idx_usuarios_usuario_modificacion", columnList = "usuarioModificacionId")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "persona_id", insertable = false, updatable = false)
    private Persona persona;

    @Column(name = "persona_id", nullable = false, unique = true)
    private Long personaId;

    @Column(nullable = false, length = 50, unique = true)
    private String nombreUsuario;

    @Column(nullable = false, length = 255)
    private String contrasenaHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RolUsuario rolCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoUsuario estadoCode;

    @Column(nullable = true)
    private LocalDateTime fechaUltimoLogin;

    @Column(nullable = true, length = 255)
    private String mfaSecret;

    @Column(nullable = false)
    @Builder.Default
    private Boolean mfaEnabled = false;

    @Column(nullable = true)
    private String mfaBackupCodes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean mfaSetupRequired = true;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    @Column(nullable = true)
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_modificacion_id", nullable = true)
    private Long usuarioModificacionId;

    public void actualizar(UpdateUsuarioDto request, Long usuarioModificacionId) {
        this.nombreUsuario = request.nombreUsuario();
        this.rolCode = request.rolCode();
        this.estadoCode = request.estadoCode();
        this.mfaEnabled = request.mfaEnabled();
        this.fechaModificacion = LocalDateTime.now();
        this.usuarioModificacionId = usuarioModificacionId;
    }

}
