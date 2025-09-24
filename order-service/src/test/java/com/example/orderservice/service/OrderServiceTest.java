package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrdersRepository;
import com.example.orderservice.service.impl.OrdersServiceImpl;
import com.example.orderservice.testData.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrdersServiceImpl ordersService;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private OrderMapper orderMapper;

    private OrderRequestDto requestDto;
    private Order order;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        requestDto = TestDataFactory.createOrderRequestDto();
    }

    @Test
    void createOrder_ShouldSaveOrderAndReturnId() {
        // given
        when(orderMapper.toEntity(requestDto)).thenReturn(order);
        when(ordersRepository.save(order)).thenReturn(order);

        // when
        UUID result = ordersService.createOrder(requestDto);

        // then
        assertEquals(orderId, result);
        verify(orderMapper).toEntity(requestDto);
        verify(ordersRepository).save(order);

        // проверим что у items есть ссылка на order
        order.getItems().forEach(item ->
                assertEquals(order, item.getOrder())
        );
    }
}
