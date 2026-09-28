package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;

import io.swagger.v3.oas.annotations.media.Schema;

public record DetailMedidorDto(
        @Schema(description = "ID del medidor", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID de la unidad", example = "10")
        Long unidadId,
        @Schema(description = "Tipo de medidor", example = "ELECTRICIDAD")
        TipoMedidor tipoMedidorCode,
        @Schema(description = "Nombre legible del tipo de medidor", example = "Electricidad")
        String tipoMedidorNombre,
        @Schema(description = "Número de serie", example = "ELEC-2026-001")
        String numeroSerie,
        @Schema(description = "Fecha de creación")
        LocalDateTime fechaCreacion,
        @Schema(description = "ID del usuario que creó el medidor", example = "1")
        Long usuarioCreacionId,
        @Schema(description = "Fecha de última modificación")
        LocalDateTime fechaModificacion,
        @Schema(description = "ID del usuario que modificó el medidor", example = "2")
        Long usuarioModificacionId) {

    public static DetailMedidorDto fromEntity(Medidor medidor) {
        return new DetailMedidorDto(
                medidor.getId(),
                medidor.getUnidadId(),
                medidor.getTipoMedidorCode(),
                medidor.getTipoMedidorCode().intoString(),
                medidor.getNumeroSerie(),
                medidor.getFechaCreacion(),
                medidor.getUsuarioCreacionId(),
                medidor.getFechaModificacion(),
                medidor.getUsuarioModificacionId());
    }
}