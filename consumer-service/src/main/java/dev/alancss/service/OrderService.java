package dev.alancss.service;

import dev.alancss.event.OrderEvent;
import dev.alancss.event.PurchaseOrderEvent;
import dev.alancss.mapper.OrderMapper;
import dev.alancss.model.Order;
import dev.alancss.repository.OrderRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class OrderService {

    private final Validator validator;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(Validator validator, OrderRepository orderRepository, OrderMapper orderMapper) {
        this.validator = validator;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public void processAndStoreOrder(PurchaseOrderEvent event) {
        var purchaseOrderData = event.getData();
        var orderEvent = purchaseOrderData.getOrder();

        Set<ConstraintViolation<OrderEvent>> violations = validator.validate(orderEvent);
        if (!violations.isEmpty()) {
            violations.forEach(violation -> log.error("Validation error for orderId {}: {}", orderEvent.getOrderId(), violation.getMessage()));
            return;
        }

        Order order = orderMapper.convertToOrder(orderEvent);

        orderRepository.save(order);
        log.info("Transaction stored successfully: {}", order);
    }

}
