package dev.alancss.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alancss.event.Order;
import dev.alancss.event.PurchaseOrder;
import dev.alancss.event.PurchaseOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class PurchaseOrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PurchaseOrderProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendPurchaseOrder(Order order) {
        try {
            String jsonData = createPurchaseOrderEvent(order);
            log.info("Sending event to purchase-orders topic");
            kafkaTemplate.send("purchase-orders", jsonData);
        } catch (JsonProcessingException e) {
            log.error("Error serializing PurchaseOrderEvent: {}", e.getMessage());
        }
    }

    private String createPurchaseOrderEvent(Order order) throws JsonProcessingException {
        var purchaseOrder = new PurchaseOrder(order, OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        var purchaseOrderEvent = new PurchaseOrderEvent(purchaseOrder);
        return objectMapper.writeValueAsString(purchaseOrderEvent);
    }

}
