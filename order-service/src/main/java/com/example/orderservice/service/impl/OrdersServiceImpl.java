package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.dto.OrderResponseDto;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrdersRepository;
import com.example.orderservice.service.OrdersService;

import jakarta.persistence.EntityNotFoundException;
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
        return ordersRepository.save(order).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return ordersRepository.findAll();
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
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
        return orderMapper.toDto(order);
    }

    @Override
    public void changeOrderStatus(UUID orderId, OrderStatus status) {
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
        order.setStatus(status);
        ordersRepository.save(order);
    }
}
