package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.service.OrdersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrdersController.class)
public class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdersService ordersService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequestDto requestDto;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        UUID customerId = UUID.fromString("b1b6c6c2-3f7b-4e44-9c1a-d0c62a3d4aef");

        OrderRequestDto.OrderItemDto item1 = OrderRequestDto.OrderItemDto.builder()
                .productId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .quantity(2)
                .build();

        OrderRequestDto.OrderItemDto item2 = OrderRequestDto.OrderItemDto.builder()
                .productId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .quantity(5)
                .build();

        requestDto = OrderRequestDto.builder()
                .customerId(customerId)
                .items(List.of(item1, item2))
                .build();

        orderId = UUID.randomUUID();
    }

    @Test
    void createOrder() throws Exception {
        when(ordersService.createOrder(any(OrderRequestDto.class)))
                .thenReturn(orderId);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + orderId.toString() + "\""));
    }

    @Test
    void deleteOrder_success() throws Exception {
        orderId = UUID.randomUUID();
        when(ordersService.deleteOrder(orderId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted successfully"));
    }

    @Test
    void deleteOrder_notFound() throws Exception {
        orderId = UUID.randomUUID();
        when(ordersService.deleteOrder(orderId)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().string("Order not found"));
    }
}

