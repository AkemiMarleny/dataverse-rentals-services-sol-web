package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Contrato;

import io.swagger.v3.oas.annotations.media.Schema;

public record DetailContratoDto(
        @Schema(description = "ID del contrato", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID de la unidad arrendada", example = "10")
        Long unidadId,
        @Schema(description = "Número de piso de la unidad", example = "5")
        Integer unidadNumeroPiso,
        @Schema(description = "Número de puerta de la unidad", example = "501")
        String unidadNumeroPuerta,
        @Schema(description = "Área de la unidad en metros cuadrados", example = "85.50")
        BigDecimal unidadAreaMetrosCuadrados,
        @Schema(description = "Tipo de acabado de la unidad", example = "ESTANDAR")
        String unidadTipoAcabados,
        @Schema(description = "ID de la persona arrendataria", example = "5")
        Long personaId,
        @Schema(description = "Denominación de la persona arrendataria", example = "Juan Carlos Pérez Gómez")
        String personaDenominacion,
        @Schema(description = "Código del tipo de documento de la persona", example = "DNI")
        String personaTipoDocumentoCodigo,
        @Schema(description = "Nombre del tipo de documento de la persona", example = "Dni")
        String personaTipoDocumentoNombre,
        @Schema(description = "Número de documento de la persona", example = "12345678")
        String personaNumeroDocumento,
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
        @Schema(description = "Código del estado del contrato", example = "VIGENTE")
        String estadoCodigo,
        @Schema(description = "Nombre legible del estado del contrato", example = "Vigente")
        String estadoNombre,
        @Schema(description = "URL del PDF del contrato digitalizado")
        String pdfUrl) {

    public static DetailContratoDto fromEntity(Contrato contrato) {
        var persona = contrato.getPersona();
        var unidad = contrato.getUnidad();
        var tipoDoc = persona.getTipoDocumentoCode();

        return new DetailContratoDto(
                contrato.getId(),
                contrato.getUnidadId(),
                unidad.getNumeroPiso(),
                unidad.getNumeroPuerta(),
                unidad.getAreaMetrosCuadrados(),
                unidad.getTipoAcabadoCode().name(),
                contrato.getPersonaId(),
                persona.getDenominacion(),
                tipoDoc.name(),
                tipoDoc.intoString(),
                persona.getNumeroDocumento(),
                contrato.getFechaInicio(),
                contrato.getFechaFin(),
                contrato.getFechaCierre(),
                contrato.getAlquilerMensual(),
                contrato.getFechaCreacion(),
                contrato.getEstado().name(),
                contrato.getEstado().intoString(),
                contrato.getPdfUrl());
    }
}