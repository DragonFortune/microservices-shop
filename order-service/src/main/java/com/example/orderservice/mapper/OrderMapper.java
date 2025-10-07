package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.dto.OrderResponseDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.OrderStatus;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Преобразуем OrderRequestDto в Order
    default Order toEntity(OrderRequestDto dto) {
        if (dto == null) return null;

        Order order = Order.builder()
                .customerId(dto.getCustomerId())
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();

        if (dto.getItems() != null) {
            List<OrderItem> items = dto.getItems().stream()
                    .map(this::toEntity)
                    .peek(item -> item.setOrder(order)) // привязываем к Order
                    .collect(Collectors.toList());
            order.setItems(items);
        }

        return order;
    }

    // Преобразуем OrderItemDto в OrderItem
    default OrderItem toEntity(OrderRequestDto.OrderItemDto dto) {
        if (dto == null) return null;

        return OrderItem.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .build();
    }

    // Преобразуем Order в OrderResponseDto
    default OrderResponseDto toDto(Order order) {
        if (order == null) return null;

        return OrderResponseDto.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(String.valueOf(order.getStatus()))
                .createdAt(order.getCreatedAt())
                .items(order.getItems() != null
                        ? order.getItems().stream().map(this::toDto).collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    // Преобразуем OrderItem в OrderItemDto
    default OrderResponseDto.OrderItemDto toDto(OrderItem item) {
        if (item == null) return null;

        return OrderResponseDto.OrderItemDto.builder()
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .build();
    }

    // Вспомогательные методы для списков (можно использовать или убрать, если выше хватает)
    default List<OrderItem> mapItems(List<OrderRequestDto.OrderItemDto> items) {
        if (items == null) return Collections.emptyList();
        return items.stream().map(this::toEntity).collect(Collectors.toList());
    }

    default List<OrderResponseDto.OrderItemDto> mapItemsDtos(List<OrderItem> items) {
        if (items == null) return Collections.emptyList();
        return items.stream().map(this::toDto).collect(Collectors.toList());
    }
}

