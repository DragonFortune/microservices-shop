package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrdersService {

    UUID createOrder(OrderRequestDto dto);
    List<Order> getAllOrders();
}
