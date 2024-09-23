package dev.alancss.service;

import dev.alancss.common.PageResponse;
import dev.alancss.event.OrderEvent;
import dev.alancss.event.PurchaseOrderData;
import dev.alancss.event.PurchaseOrderEvent;
import dev.alancss.exception.ResourceNotFoundException;
import dev.alancss.mapper.OrderMapper;
import dev.alancss.model.Order;
import dev.alancss.repository.OrderRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private Validator validator;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessAndStoreOrder_ValidOrder() {
        var orderEvent = new OrderEvent(
                "250fbfed-d6a9-4466-9cca-c3d26ed867f4",
                "customer456",
                100.0,
                "PAID",
                "2024-09-23T12:00:00"
        );

        var purchaseOrderData = new PurchaseOrderData(orderEvent, "2024-09-23T12:00:00");
        var purchaseOrderEvent = new PurchaseOrderEvent(purchaseOrderData);

        Order order = mock(Order.class);
        when(validator.validate(orderEvent)).thenReturn(Collections.emptySet());
        when(orderMapper.convertToOrder(orderEvent)).thenReturn(order);

        orderService.processAndStoreOrder(purchaseOrderEvent);

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testProcessAndStoreOrder_InvalidOrder() {
        OrderEvent orderEvent = new OrderEvent(
                "250fbfed-d6a9-4466-9cca-c3d26ed867f4", // Invalid orderId
                "250lkled-d6a9-4466-9cca-c3d98cd867f4",
                -100.0,
                "PAID",
                "2024-09-23T12:00:00"
        );

        var purchaseOrderData = new PurchaseOrderData(orderEvent, "2024-09-23T12:00:00");
        var purchaseOrderEvent = mock(PurchaseOrderEvent.class);

        when(purchaseOrderEvent.getData()).thenReturn(purchaseOrderData);

        ConstraintViolation<OrderEvent> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Invalid orderId. Must not be blank");
        when(validator.validate(orderEvent)).thenReturn(Set.of(violation));

        orderService.processAndStoreOrder(purchaseOrderEvent);

        verify(orderRepository, never()).save(any(Order.class));
    }


    @Test
    void testFindAllOrders() {
        // Configuração do pageable
        Pageable pageable = PageRequest.of(0, 10);
        Order order = mock(Order.class);
        PageImpl<Order> orders = new PageImpl<>(List.of(order), pageable, 1);

        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orders);

        PageResponse<Order> result = orderService.findAllOrders(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(order, result.getContent().getFirst());
    }

    @Test
    void testFindByOrderId_OrderExists() {
        String orderId = "123";
        Order order = mock(Order.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.findByOrderId(orderId);

        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    void testFindByOrderId_OrderNotFound() {
        String orderId = "123";

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.findByOrderId(orderId));
    }

    @Test
    void testFindByCustomerId() {
        String customerId = "customer-123";
        Order order = mock(Order.class);

        when(orderRepository.findByCustomerId(customerId)).thenReturn(List.of(order));

        List<Order> result = orderService.findByCustomerId(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByStatus() {
        String status = "PAID";
        Order order = mock(Order.class);

        when(orderRepository.findByStatusIgnoreCase(status)).thenReturn(List.of(order));

        List<Order> result = orderService.findByStatus(status);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(order, result.getFirst());
    }

    @Test
    void testDeleteById_OrderExists() {
        String orderId = "123";
        Order order = mock(Order.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.deleteById(orderId);

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void testDeleteById_OrderNotFound() {
        String orderId = "123";

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteById(orderId));
        verify(orderRepository, never()).delete(any(Order.class));
    }
}
