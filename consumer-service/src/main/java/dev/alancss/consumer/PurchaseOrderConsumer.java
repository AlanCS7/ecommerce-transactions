package dev.alancss.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alancss.event.PurchaseOrderEvent;
import dev.alancss.service.LoggingService;
import dev.alancss.service.MetricsService;
import dev.alancss.service.OrderService;
import dev.alancss.util.MDCContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PurchaseOrderConsumer {

    private final OrderService orderService;
    private final MetricsService metricsService;
    private final LoggingService loggingService;

    public PurchaseOrderConsumer(OrderService orderService, MetricsService metricsService, ObjectMapper objectMapper, LoggingService loggingService) {
        this.orderService = orderService;
        this.metricsService = metricsService;
        this.loggingService = loggingService;
    }

    @KafkaListener(topics = "purchase-orders", groupId = "purchase-orders-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, PurchaseOrderEvent> record, Acknowledgment acknowledgment) {
        MDCContextHelper.setMDC(record);

        metricsService.recordConsumptionTime(() -> {
            PurchaseOrderEvent event = record.value();

            loggingService.logEvent(event);

            metricsService.recordExecutionTime(() -> {
                metricsService.recordPersistenceTime(() -> {
                    orderService.processAndStoreOrder(event);
                });
            });

            metricsService.recordAckTime(acknowledgment::acknowledge);
        });

        MDCContextHelper.clearMDC();
    }
}

