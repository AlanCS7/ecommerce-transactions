package dev.alancss.event;

public record PurchaseOrder(
        Order order,
        String timestamp
) {
}
