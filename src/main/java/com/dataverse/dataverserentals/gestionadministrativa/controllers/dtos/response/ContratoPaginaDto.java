package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Contrato;
import com.dataverse.dataverserentals.gestionadministrativa.entities.VContrato;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoContrato;

public record ContratoPaginaDto(
                Long id,
                Long unidadId,
                Long personaId,
                String personaDenominacion,
                LocalDateTime fechaInicio,
                LocalDateTime fechaFin,
                LocalDateTime fechaCierre,
                BigDecimal alquilerMensual,
                LocalDateTime fechaCreacion,
                EstadoContrato estadoCode,
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
