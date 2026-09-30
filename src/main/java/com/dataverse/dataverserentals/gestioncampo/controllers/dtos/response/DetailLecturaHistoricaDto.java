package com.dataverse.dataverserentals.gestioncampo.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;
import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;

import io.swagger.v3.oas.annotations.media.Schema;

public record DetailLecturaHistoricaDto(
        @Schema(description = "ID de la lectura histórica", example = "40", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "ID del edificio", example = "1")
        Long edificioId,
        @Schema(description = "Nombre del edificio", example = "Torre Central")
        String edificioNombre,
        @Schema(description = "ID de la unidad", example = "10")
        Long unidadId,
        @Schema(description = "Número de puerta de la unidad", example = "501")
        String unidadNumeroPuerta,
        @Schema(description = "ID del medidor", example = "20")
        Long medidorId,
        @Schema(description = "Tipo de medidor (GENERAL_ELECTRICIDAD, GENERAL_AGUA, ELECTRICIDAD, AGUA)", example = "ELECTRICIDAD")
        TipoMedidor tipoMedidorCode,
        @Schema(description = "Nombre legible del tipo de medidor", example = "Electricidad")
        String tipoMedidorNombre,
        @Schema(description = "Número de serie del medidor", example = "ELEC-2026-001")
        String medidorNumeroSerie,
        @Schema(description = "Fecha de la lectura", example = "2026-01-15")
        LocalDate fechaLectura,
        @Schema(description = "Valor de la lectura", example = "1234.56")
        BigDecimal valorLectura,
        @Schema(description = "Fecha de registro de la lectura")
        LocalDateTime fechaCreacion,
        @Schema(description = "ID del usuario que registró la lectura", example = "1")
        Long usuarioCreacionId) {

    public static DetailLecturaHistoricaDto fromEntity(LecturaHistorica lecturaHistorica) {
        return new DetailLecturaHistoricaDto(
                lecturaHistorica.getId(),
                lecturaHistorica.getMedidor().getUnidad().getEdificioId(),
                lecturaHistorica.getMedidor().getUnidad().getEdificio().getNombre(),
                lecturaHistorica.getMedidor().getUnidadId(),
                lecturaHistorica.getMedidor().getUnidad().getNumeroPuerta(),
                lecturaHistorica.getMedidorId(),
                lecturaHistorica.getMedidor().getTipoMedidorCode(),
                lecturaHistorica.getMedidor().getTipoMedidorCode().intoString(),
                lecturaHistorica.getMedidor().getNumeroSerie(),
                lecturaHistorica.getFechaLectura(),
                lecturaHistorica.getValorLectura(),
                lecturaHistorica.getFechaCreacion(),
                lecturaHistorica.getUsuarioCreacionId());
    }

}