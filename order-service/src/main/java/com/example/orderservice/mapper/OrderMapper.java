package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.dto.OrderResponseDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper( componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "customerId", target = "customerId")
    Order toEntity(OrderRequestDto dto);

    OrderItem toEntity(OrderRequestDto.OrderItemDto dto);

    OrderResponseDto toDto(Order order);

    OrderResponseDto.OrderItemDto toDto(OrderItem item);

    List<OrderItem> mapItems(List<OrderRequestDto.OrderItemDto> items);

    List<OrderResponseDto.OrderItemDto> mapItemsDtos(List<OrderItem> items);
}
