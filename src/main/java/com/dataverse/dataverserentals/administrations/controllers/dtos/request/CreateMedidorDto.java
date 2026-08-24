package com.dataverse.dataverserentals.administrations.controllers.dtos.request;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import com.dataverse.dataverserentals.administrations.enums.TipoMedidor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMedidorDto(
        @NotNull Long unidadId,
        @NotNull TipoMedidor tipoMedidorCode,
        @NotBlank @Length(min = 7) String numeroSerie,
        @NotNull LocalDateTime fechaCreacion,
        @NotNull Long usuarioCreacionId,
        LocalDateTime fechaModificacion,
        Long usuarioModificacionId) {
}
