package com.dataverse.dataverserentals.notifications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.dataverse.dataverserentals.common.dto.EventNotification;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);

        emitter.onCompletion(() -> {
            log.info("SSE emitter completed");
            emitters.remove(emitter);
        });

        emitter.onTimeout(() -> {
            log.info("SSE emitter timeout");
            emitter.complete();
            emitters.remove(emitter);
        });

        emitter.onError((ex) -> {
            log.error("SSE emitter error", ex);
            emitters.remove(emitter);
        });

        emitters.add(emitter);
        log.info("Nuevo SSE emitter registrado. Total conectados: {}", emitters.size());

        return emitter;
    }

    public void sendEvent(String eventType, Object data) {
        EventNotification event = EventNotification.of(eventType, data);
        List<SseEmitter> deadEmitters = new ArrayList<>();

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventType)
                        .data(event));
            } catch (IOException e) {
                log.error("Error enviando evento SSE a emitter", e);
                deadEmitters.add(emitter);
            }
        });

        emitters.removeAll(deadEmitters);
        log.info("Evento {} enviado a {} clientes", eventType, emitters.size() - deadEmitters.size());
    }
}
