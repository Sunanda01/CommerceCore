package com.CommerceCore.service;

import com.CommerceCore.dto.OrderDto;
import com.CommerceCore.dto.OrderItemDto;
import com.CommerceCore.entity.*;
import com.CommerceCore.repository.OrderItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepo orderItemRepo;
    public OrderItemDto mapToDto(OrderItem orderItem){
        // Entity => DTO
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .productId(orderItem.getProduct() != null ? orderItem.getProduct().getId() : null)
                .productName(orderItem.getProduct() != null ? orderItem.getProduct().getName() : null)
                .build();
    }

    // DTO => Entity
    public OrderItem mapToEntity(OrderItemDto dto, Product product, Order order){
        return OrderItem.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .product(product)
                .order(order)
                .build();
    }
}
