package com.CommerceCore.dto;

import com.CommerceCore.entity.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private String status;
    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemDto> items;
}
