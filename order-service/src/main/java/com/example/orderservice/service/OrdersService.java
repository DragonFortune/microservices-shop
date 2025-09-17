package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequestDto;

import java.util.UUID;

public interface OrdersService {

    UUID createOrder(OrderRequestDto dto);
}
