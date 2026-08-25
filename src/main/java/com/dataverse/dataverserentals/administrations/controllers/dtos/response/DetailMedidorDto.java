package com.dataverse.dataverserentals.administrations.controllers.dtos.response;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.administrations.entities.Medidor;
import com.dataverse.dataverserentals.administrations.enums.TipoMedidor;

public record DetailMedidorDto(
        Long id,
        Long edificioId,
        String nombreEdificio,
        Long unidadId,
        Integer numeroPisoUnidad,
        String numeroPuertaUnidad,
        TipoMedidor tipoMedidorCode,
        String numeroSerie,
        LocalDateTime fechaCreacion,
        Long usuarioCreacionId,
        LocalDateTime fechaModificacion,
        Long usuarioModificacionId) {

    public static DetailMedidorDto fromEntity(Medidor medidor) {
        return new DetailMedidorDto(
                medidor.getId(),
                medidor.getUnidad().getEdificioId(),
                medidor.getUnidad().getEdificio().getNombre(),
                medidor.getUnidadId(),
                medidor.getUnidad().getNumeroPiso(),
                medidor.getUnidad().getNumeroPuerta(),
                medidor.getTipoMedidorCode(),
                medidor.getNumeroSerie(),
                medidor.getFechaCreacion(),
                medidor.getUsuarioCreacionId(),
                medidor.getFechaModificacion(),
                medidor.getUsuarioModificacionId());
    }

}
