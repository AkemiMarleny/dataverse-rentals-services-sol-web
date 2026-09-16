package com.dataverse.dataverserentals.common.dto;

import java.time.LocalDateTime;

public record EventNotification(String eventType, Object data, LocalDateTime timestamp) {
    public static EventNotification of(String eventType, Object data) {
        return new EventNotification(eventType, data, LocalDateTime.now());
    }
}
