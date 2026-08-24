package com.dataverse.dataverserentals.administrations.controllers.dtos.response;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.administrations.entities.Medidor;
import com.dataverse.dataverserentals.administrations.enums.TipoMedidor;

public record MedidorPaginaDto(
        Long id,
        Long unidadId,
        TipoMedidor tipoMedidorCode,
        String numeroSerie,
        LocalDateTime fechaCreacion,
        Long usuarioCreacionId,
        LocalDateTime fechaModificacion,
        Long usuarioModificacionId) {

    public static MedidorPaginaDto fromEntity(Medidor medidor) {
        return new MedidorPaginaDto(
                medidor.getId(),
                medidor.getUnidadId(),
                medidor.getTipoMedidorCode(),
                medidor.getNumeroSerie(),
                medidor.getFechaCreacion(),
                medidor.getUsuarioCreacionId(),
                medidor.getFechaModificacion(),
                medidor.getUsuarioModificacionId());
    }

}
