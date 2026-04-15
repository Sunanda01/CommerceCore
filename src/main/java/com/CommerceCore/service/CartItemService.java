package com.CommerceCore.service;

import com.CommerceCore.dto.CartItemDto;
import com.CommerceCore.dto.OrderDto;
import com.CommerceCore.entity.*;
import com.CommerceCore.repository.CartItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepo cartItemRepo;
    public CartItemDto mapToDto(CartItem cartItem){
        // Entity => DTO
        return CartItemDto.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .productId(cartItem.getProduct() != null ? cartItem.getProduct().getId() : null)
                .productName(cartItem.getProduct() != null ? cartItem.getProduct().getName() : null)
                .price(cartItem.getProduct() != null ? cartItem.getProduct().getPrice() : null)
                .build();
    }

    // DTO => Entity
    public CartItem mapToEntity(CartItemDto dto, Product product, User user){
        return CartItem.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .user(user)
                .product(product)
                .build();
    }
}
