package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;

public record DetailMedidorDto(
        Long id,
        Long unidadId,
        TipoMedidor tipoMedidorCode,
        String numeroSerie,
        LocalDateTime fechaCreacion,
        Long usuarioCreacionId,
        LocalDateTime fechaModificacion,
        Long usuarioModificacionId) {

    public static DetailMedidorDto fromEntity(Medidor medidor) {
        return new DetailMedidorDto(
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
