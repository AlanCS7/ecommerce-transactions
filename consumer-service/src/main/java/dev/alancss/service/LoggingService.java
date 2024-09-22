package dev.alancss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alancss.event.PurchaseOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingService {

    private final ObjectMapper objectMapper;

    public LoggingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void logEvent(PurchaseOrderEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            log.info(eventJson);
        } catch (JsonProcessingException e) {
            log.error("Error serializing PurchaseOrderEvent to JSON", e);
        }
    }
}
