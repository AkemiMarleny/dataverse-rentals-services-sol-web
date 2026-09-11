package com.dataverse.dataverserentals.security.config;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UsuarioRepository;
import com.dataverse.dataverserentals.security.services.MfaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MfaSecretMigration {

    private final UsuarioRepository usuarioRepository;
    private final MfaSecretEncryptor mfaSecretEncryptor;
    private final MfaService mfaService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void migratePlaintextSecrets() {
        List<Usuario> usuariosWithMfa = usuarioRepository.findAllByMfaSecretIsNotNullAndMfaEnabledIsTrue();
        int migratedSecrets = 0;
        int migratedBackupCodes = 0;

        for (Usuario usuario : usuariosWithMfa) {
            String secret = usuario.getMfaSecret();
            if (mfaService.isPlaintextBase32(secret)) {
                usuario.setMfaSecret(mfaSecretEncryptor.encrypt(secret));
                migratedSecrets++;
                log.info("Migrated MFA secret for user: {}", usuario.getNombreUsuario());
            }

            String backupCodes = usuario.getMfaBackupCodes();
            if (backupCodes != null && backupCodes.contains(",")) {
                usuario.setMfaBackupCodes(mfaSecretEncryptor.encrypt(backupCodes));
                migratedBackupCodes++;
                log.info("Migrated backup codes for user: {}", usuario.getNombreUsuario());
            }

            usuarioRepository.save(usuario);
        }

        if (migratedSecrets > 0 || migratedBackupCodes > 0) {
            log.info("MFA migration completed. Migrated {} secrets and {} backup codes.", migratedSecrets, migratedBackupCodes);
        }
    }
}
