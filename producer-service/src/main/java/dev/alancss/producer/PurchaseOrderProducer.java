package dev.alancss.producer;

import dev.alancss.event.Order;
import dev.alancss.event.PurchaseOrder;
import dev.alancss.event.PurchaseOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class PurchaseOrderProducer {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final KafkaTemplate<String, PurchaseOrderEvent> kafkaTemplate;
    private final Random random = new Random();

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

    public void sendPurchaseOrderInBatch(int batchSize) {
        for (int i = 0; i < batchSize; i++) {
            String orderId = UUID.randomUUID().toString();
            String customerId = UUID.randomUUID().toString();
            double amount = random.nextDouble() * 200 - 100;
            var randomDate = LocalDateTime.now().minusDays(random.nextInt(30));
            String date = randomDate.format(formatter);

            var order = new Order(orderId, customerId, amount, getRandomStatus(), date);

            sendPurchaseOrder(order);
        }
    }

    private String getRandomStatus() {
        String[] statuses = {"PAID", "PENDING", "FAILED", "ERROR"};
        return statuses[random.nextInt(statuses.length)];
    }
}
