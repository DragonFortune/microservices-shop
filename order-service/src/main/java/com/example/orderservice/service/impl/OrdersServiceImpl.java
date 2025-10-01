package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.dto.OrderResponseDto;
import com.example.orderservice.exception.types.ConflictException;
import com.example.orderservice.exception.types.NotFoundException;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrdersRepository;
import com.example.orderservice.service.OrdersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public UUID createOrder(OrderRequestDto dto) {
        log.info("Creating order for customerId={} with {} items", dto.getCustomerId(), dto.getItems().size());

        Order order = orderMapper.toEntity(dto);
        order.getItems().forEach(item -> item.setOrder(order));
        order.setStatus(OrderStatus.CREATED);

        log.info("Order created successfully with id={}", order.getId());

        return ordersRepository.save(order).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        log.info("Fetched {} orders", ordersRepository.findAll().size());
        return ordersRepository.findAll().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public boolean deleteOrder(UUID orderId) {
        if (!ordersRepository.existsById(orderId)) {
            return false;
        }
        ordersRepository.deleteById(orderId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(UUID orderId) {
        log.info("Fetching order with id={}", orderId);

        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found: {}", orderId);
                    return new NotFoundException("Order not found: " + orderId);
                });

        return orderMapper.toDto(order);
    }

    @Override
    public void changeOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        OrderStatus currentStatus = order.getStatus();

        if (!isValidStatus(currentStatus, newStatus)) {
            throw new ConflictException(
                    "Invalid status transition " + currentStatus + " -> " + newStatus
            );
        }

        order.setStatus(newStatus);
        ordersRepository.save(order);
    }

    private boolean isValidStatus(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case CREATED -> (next == OrderStatus.PAID || next == OrderStatus.CANCELLED);
            case PAID -> next == OrderStatus.SHIPPED;
            default -> false;
        };
    }
}
