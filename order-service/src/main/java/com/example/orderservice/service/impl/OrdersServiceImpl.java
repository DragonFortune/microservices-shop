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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public UUID createOrder(OrderRequestDto dto) {
        Order order = orderMapper.toEntity(dto);
        order.getItems().forEach(item -> item.setOrder(order));
        order.setStatus(OrderStatus.CREATED);
        return ordersRepository.save(order).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
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
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
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
