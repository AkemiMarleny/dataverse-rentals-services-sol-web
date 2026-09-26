package com.dataverse.dataverserentals.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateResponse(
        @Schema(description = "ID del recurso creado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id) {
}