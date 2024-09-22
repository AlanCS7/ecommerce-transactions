package dev.alancss.util;

import dev.alancss.event.PurchaseOrderEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;

public class MDCContextHelper {

    public static void setMDC(ConsumerRecord<String, PurchaseOrderEvent> record) {
        MDC.put("schema", "PurchaseOrderEvent");
        MDC.put("offset", String.valueOf(record.offset()));
        MDC.put("partition", String.valueOf(record.partition()));
        MDC.put("topic", record.topic());
        MDC.put("timestamp", record.value().getData().getTimestamp());
        MDC.put("timestampType", record.timestampType().name());
    }

    public static void clearMDC() {
        MDC.clear();
    }
}
