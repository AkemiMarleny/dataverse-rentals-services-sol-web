package com.dataverse.dataverserentals.gestioncampo.controllers.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;
import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;

public record LecturaHistoricaPaginaDto(
        Long id,
        Long edificioId,
        String edificioNombre,
        Long unidadId,
        String unidadNumeroPuerta,
        Long medidorId,
        TipoMedidor tipoMedidorCode,
        String tipoMedidorNombre,
        String medidorNumeroSerie,
        LocalDate fechaLectura,
        BigDecimal valorLectura,
        LocalDateTime fechaCreacion,
        Long usuarioCreacionId) {

    public static LecturaHistoricaPaginaDto fromEntity(LecturaHistorica lecturaHistorica) {
        return new LecturaHistoricaPaginaDto(
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
