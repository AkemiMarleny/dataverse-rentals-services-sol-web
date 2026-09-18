package com.dataverse.dataverserentals.gestioncampo.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;

public record DetailLecturaHistoricaDto(
        Long id,
        Long medidorId,
        LocalDate fechaLectura,
        BigDecimal valorLectura,
        LocalDateTime fechaCreacion,
        Long usuarioCreacionId) {

    public static DetailLecturaHistoricaDto fromEntity(LecturaHistorica lecturaHistorica) {
        return new DetailLecturaHistoricaDto(
                lecturaHistorica.getId(),
                lecturaHistorica.getMedidorId(),
                lecturaHistorica.getFechaLectura(),
                lecturaHistorica.getValorLectura(),
                lecturaHistorica.getFechaCreacion(),
                lecturaHistorica.getUsuarioCreacionId());
    }

}
