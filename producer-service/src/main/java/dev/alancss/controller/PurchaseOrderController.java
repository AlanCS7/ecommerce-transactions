package dev.alancss.controller;

import dev.alancss.event.Order;
import dev.alancss.producer.PurchaseOrderProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderProducer purchaseOrderProducer;

    public PurchaseOrderController(PurchaseOrderProducer purchaseOrderProducer) {
        this.purchaseOrderProducer = purchaseOrderProducer;
    }

    @PostMapping
    public ResponseEntity<?> sendEvent(@RequestBody Order order) {
        purchaseOrderProducer.sendPurchaseOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/batch")
    public ResponseEntity<?> sendEventInBatch(@RequestParam int batchSize) {
        purchaseOrderProducer.sendPurchaseOrderInBatch(batchSize);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
