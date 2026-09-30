package com.dataverse.dataverserentals.notifications;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Suscripción a notificaciones en tiempo real (Server-Sent Events)")
public class EventsController {

    private final NotificationService notificationService;

    @GetMapping("/subscribe")
    @Operation(
            summary = "Suscribirse a notificaciones en tiempo real",
            description = "Abre una conexión Server-Sent Events (text/event-stream) para recibir notificaciones en tiempo real. El token JWT se envía en el header Authorization; la conexión permanece abierta hasta que el cliente la cierre.")
    @ApiResponse(responseCode = "200", description = "Stream de eventos abierto (text/event-stream)")
    public SseEmitter subscribe() {
        log.info("Nueva suscripción SSE recibida");
        return notificationService.subscribe();
    }
}