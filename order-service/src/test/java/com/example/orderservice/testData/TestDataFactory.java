package com.example.orderservice.testData;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TestDataFactory {

    private static final UUID CUSTOMER_ID = UUID.fromString("b1b6c6c2-3f7b-4e44-9c1a-d0c62a3d4aef");

    public static OrderRequestDto createOrderRequestDto() {
        OrderRequestDto.OrderItemDto item1 = OrderRequestDto.OrderItemDto.builder()
                .productId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .quantity(2)
                .build();

        OrderRequestDto.OrderItemDto item2 = OrderRequestDto.OrderItemDto.builder()
                .productId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .quantity(5)
                .build();

        return OrderRequestDto.builder()
                .customerId(CUSTOMER_ID)
                .items(List.of(item1,item2))
                .build();
    }

    public static Order createOrderEntity() {
        Order order = new Order();
        order.setCustomerId(CUSTOMER_ID);
        order.setId(UUID.randomUUID());
        order.setItems(createOrderRequestDto().getItems().stream()
                .map(dto -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(dto.getProductId());
                    item.setQuantity(dto.getQuantity());
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toList()));
        return order;
    }
}
