package com.CommerceCore.dto;

import com.CommerceCore.entity.OrderStatus;
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

    private List<OrderItemDto> items;
}
