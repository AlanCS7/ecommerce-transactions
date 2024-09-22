package dev.alancss.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final MeterRegistry meterRegistry;

    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordConsumptionTime(Runnable task) {
        Timer.builder("purchase.order.consumption.time")
                .description("Time taken to consume the purchase order event")
                .publishPercentiles(0.5, 0.99)
                .register(meterRegistry)
                .record(task);
    }

    public void recordPersistenceTime(Runnable task) {
        Timer.builder("purchase.order.persistence.time")
                .description("Time taken to persist purchase orders")
                .publishPercentiles(0.5, 0.99)
                .register(meterRegistry)
                .record(task);
    }

    public void recordExecutionTime(Runnable task) {
        Timer.builder("purchase.order.execution.time")
                .description("Total execution time for processing the order")
                .publishPercentiles(0.5, 0.99)
                .register(meterRegistry)
                .record(task);
    }

    public void recordAckTime(Runnable task) {
        Timer.builder("purchase.order.acknowledge.time")
                .description("Time taken to acknowledge the purchase order event")
                .publishPercentiles(0.5, 0.99)
                .register(meterRegistry)
                .record(task);
    }
}
