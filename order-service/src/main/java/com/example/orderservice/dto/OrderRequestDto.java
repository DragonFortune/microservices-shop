package com.example.orderservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {

    @NotNull
    private UUID customerId;

    @NotEmpty
    private List <OrderItemDto> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemDto {
        @NotNull
        private UUID productId;

        @NotNull
        private Integer quantity;
    }
}
