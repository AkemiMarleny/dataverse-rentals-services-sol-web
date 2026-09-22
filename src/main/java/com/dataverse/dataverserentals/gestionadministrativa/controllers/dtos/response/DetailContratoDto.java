package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Contrato;

public record DetailContratoDto(
        Long id,
        Long unidadId,
        Integer unidadNumeroPiso,
        String unidadNumeroPuerta,
        BigDecimal unidadAreaMetrosCuadrados,
        String unidadTipoAcabados,
        Long personaId,
        String personaDenominacion,
        String personaTipoDocumentoCodigo,
        String personaTipoDocumentoNombre,
        String personaNumeroDocumento,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        LocalDateTime fechaCierre,
        BigDecimal alquilerMensual,
        LocalDateTime fechaCreacion,
        String estadoCodigo,
        String estadoNombre,
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
