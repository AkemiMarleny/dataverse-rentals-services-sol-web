package com.dataverse.dataverserentals.gestioncampo.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;

public record LecturaHistoricaPaginaDto(
        Long id,
        Long medidorId,
        LocalDateTime fechaLectura,
        BigDecimal valorLectura,
        LocalDateTime fechaCreacion,
        Long usuarioCreacionId) {

    public static LecturaHistoricaPaginaDto fromEntity(LecturaHistorica lecturaHistorica) {
        return new LecturaHistoricaPaginaDto(
                lecturaHistorica.getId(),
                lecturaHistorica.getMedidorId(),
                lecturaHistorica.getFechaLectura(),
                lecturaHistorica.getValorLectura(),
                lecturaHistorica.getFechaCreacion(),
                lecturaHistorica.getUsuarioCreacionId());
    }

}
