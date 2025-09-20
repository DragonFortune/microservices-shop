package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.testData.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderMapperTest {

    private OrderRequestDto requestDto;
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @BeforeEach
    void setUp() {
        requestDto = TestDataFactory.createOrderRequestDto();
    }

    @Test
    void testToEntity() {
        Order order = orderMapper.toEntity(requestDto);

        assertNotNull(order);
        assertEquals(requestDto.getCustomerId(), order.getCustomerId());
        assertNotNull(order.getItems());
        assertEquals(requestDto.getItems().size(), order.getItems().size());

        for (int i = 0; i <requestDto.getItems().size(); i++) {
            assertEquals(requestDto.getItems().get(i).getProductId(), order.getItems().get(i).getProductId());
            assertEquals(requestDto.getItems().get(i).getQuantity(), order.getItems().get(i).getQuantity());
        }
    }
}
