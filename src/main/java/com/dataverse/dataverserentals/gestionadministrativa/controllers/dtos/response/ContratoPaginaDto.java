package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Contrato;
import com.dataverse.dataverserentals.gestionadministrativa.entities.VContrato;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoContrato;

import io.swagger.v3.oas.annotations.media.Schema;

public record ContratoPaginaDto(
        @Schema(description = "ID del contrato", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID de la unidad arrendada", example = "10")
        Long unidadId,
        @Schema(description = "ID de la persona arrendataria", example = "5")
        Long personaId,
        @Schema(description = "Denominación de la persona arrendataria", example = "Juan Carlos Pérez Gómez")
        String personaDenominacion,
        @Schema(description = "Fecha de inicio del contrato")
        LocalDateTime fechaInicio,
        @Schema(description = "Fecha de fin del contrato")
        LocalDateTime fechaFin,
        @Schema(description = "Fecha de cierre del contrato")
        LocalDateTime fechaCierre,
        @Schema(description = "Alquiler mensual", example = "1200.00")
        BigDecimal alquilerMensual,
        @Schema(description = "Fecha de creación del contrato")
        LocalDateTime fechaCreacion,
        @Schema(description = "Estado del contrato (VIGENTE, POR_VENCER, VENCIDO, CULMINADO)", example = "VIGENTE")
        EstadoContrato estadoCode,
        @Schema(description = "Nombre legible del estado del contrato", example = "Vigente")
        String estadoNombre) {

        public static ContratoPaginaDto fromEntity(Contrato contrato) {
                return new ContratoPaginaDto(
                                contrato.getId(),
                                contrato.getUnidadId(),
                                contrato.getPersonaId(),
                                contrato.getPersona()
                                                .getDenominacion(),
                                contrato.getFechaInicio(),
                                contrato.getFechaFin(),
                                contrato.getFechaCierre(),
                                contrato.getAlquilerMensual(),
                                contrato.getFechaCreacion(),
                                contrato.getEstado(),
                                contrato.getEstado()
                                                .intoString());
        }

        public static ContratoPaginaDto fromEntity(VContrato contrato) {
                return new ContratoPaginaDto(
                                contrato.getId(),
                                contrato.getUnidadId(),
                                contrato.getPersonaId(),
                                contrato.getPersona()
                                                .getDenominacion(),
                                contrato.getFechaInicio(),
                                contrato.getFechaFin(),
                                contrato.getFechaCierre(),
                                contrato.getAlquilerMensual(),
                                contrato.getFechaCreacion(),
                                contrato.getEstado(),
                                contrato.getEstado()
                                                .intoString());
        }
}