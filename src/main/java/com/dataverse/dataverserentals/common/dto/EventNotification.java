package com.dataverse.dataverserentals.common.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record EventNotification(
        @Schema(description = "Tipo del evento", example = "CONTRATO_POR_VENCER")
        String eventType,
        @Schema(description = "Datos del evento (dependen del tipo)")
        Object data,
        @Schema(description = "Marca de tiempo del evento")
        LocalDateTime timestamp) {
    public static EventNotification of(String eventType, Object data) {
        return new EventNotification(eventType, data, LocalDateTime.now());
    }
}