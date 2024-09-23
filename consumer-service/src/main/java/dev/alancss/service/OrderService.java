package dev.alancss.service;

import dev.alancss.common.PageResponse;
import dev.alancss.event.OrderEvent;
import dev.alancss.event.PurchaseOrderEvent;
import dev.alancss.exception.ResourceNotFoundException;
import dev.alancss.mapper.OrderMapper;
import dev.alancss.model.Order;
import dev.alancss.repository.OrderRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public PageResponse<Order> findAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orders = orderRepository.findAll(pageable);

        return new PageResponse<>(
                orders.getContent(),
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages(),
                orders.isFirst(),
                orders.isLast()
        );
    }

    public Order findByOrderId(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with ID [%s] not found".formatted(orderId)));
    }

    public List<Order> findByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> findByStatus(String status) {
        return orderRepository.findByStatusIgnoreCase(status);
    }

    public void deleteById(String orderId) {
        orderRepository.findById(orderId).ifPresentOrElse(
                orderRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Order with ID [%s] not found".formatted(orderId));
                }
        );
    }
}
