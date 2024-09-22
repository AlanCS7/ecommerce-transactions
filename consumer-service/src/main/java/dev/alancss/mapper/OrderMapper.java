package dev.alancss.mapper;

import dev.alancss.event.OrderEvent;
import dev.alancss.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order convertToOrder(OrderEvent orderEvent) {
        return new Order(
                orderEvent.getOrderId(),
                orderEvent.getCustomerId(),
                orderEvent.getAmount(),
                orderEvent.getStatus(),
                orderEvent.getDate()
        );
    }
}
