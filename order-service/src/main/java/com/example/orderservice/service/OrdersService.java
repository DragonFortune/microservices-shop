package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.dto.OrderResponseDto;
import com.example.orderservice.model.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrdersService {

    UUID createOrder(OrderRequestDto dto);
    List<OrderResponseDto> getAllOrders();
    boolean deleteOrder(UUID orderId);
    OrderResponseDto getOrder(UUID orderId);
    void changeOrderStatus (UUID orderId, OrderStatus status);
}
