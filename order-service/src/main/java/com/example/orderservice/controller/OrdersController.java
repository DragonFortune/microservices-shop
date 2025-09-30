package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.dto.OrderResponseDto;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.service.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @PostMapping()
    public UUID createOrder(@RequestBody @Valid OrderRequestDto dto) {
        return ordersService.createOrder(dto);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID id) {
        if (ordersService.deleteOrder(id)) {
            return  ResponseEntity.ok("Order deleted successfully");
        } else {
            return ResponseEntity.ok("Order not found");
        }
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@PathVariable("id") UUID orderId) {
        return ordersService.getOrder(orderId);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeOrderStatus(
            @PathVariable("id") UUID orderId,
            @RequestParam("status")OrderStatus status
            ) {
        ordersService.changeOrderStatus(orderId, status);
        return ResponseEntity.noContent().build();
    }
}
