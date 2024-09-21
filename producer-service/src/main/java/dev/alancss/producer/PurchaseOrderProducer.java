package dev.alancss.producer;

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

    private final KafkaTemplate<String, PurchaseOrderEvent> kafkaTemplate;

    public PurchaseOrderProducer(KafkaTemplate<String, PurchaseOrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPurchaseOrder(Order order) {
        String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        var purchaseOrder = new PurchaseOrder(order, timestamp);
        var purchaseOrderEvent = new PurchaseOrderEvent(purchaseOrder);

        log.info("Sending event to purchase-orders topic");
        kafkaTemplate.send("purchase-orders", purchaseOrderEvent);
    }

}
