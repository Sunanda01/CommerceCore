package com.CommerceCore.service;

import com.CommerceCore.dto.OrderDto;
import com.CommerceCore.dto.ProductDto;
import com.CommerceCore.entity.*;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    public OrderDto mapToDto(Order order){
        // Entity => DTO
        return OrderDto.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus().name())  // enum => string
                .build();
    }

    // DTO => Entity
    public Order mapToEntity(OrderDto dto, User user){
        return Order.builder()
                .id(dto.getId())
                .totalAmount(dto.getTotalAmount())
                .createdAt(dto.getCreatedAt())
                .status(OrderStatus.PENDING)
                .user(user)
                .build();
    }
}
