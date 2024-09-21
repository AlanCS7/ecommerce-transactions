package dev.alancss.event;

public record Order(
        String orderId,
        String customerId,
        double amount,
        String status,
        String date
) {
}
