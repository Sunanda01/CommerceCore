package com.CommerceCore.service;

import com.CommerceCore.dto.CartItemDto;
import com.CommerceCore.dto.OrderDto;
import com.CommerceCore.entity.*;
import com.CommerceCore.exception.ApiException;
import com.CommerceCore.repository.CartItemRepo;
import com.CommerceCore.repository.ProductRepo;
import com.CommerceCore.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepo cartItemRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    // Add To Cart
    public CartItemDto addToCart(Long userId, Long productId, int quantity){
        User user=userRepo.findById(userId).orElseThrow(()->new ApiException("User Not Found", HttpStatus.NOT_FOUND));
        Product product=productRepo.findById(productId).orElseThrow(()->new ApiException("Product Not Found",HttpStatus.NOT_FOUND));
        CartItem cartItem=CartItem.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();
        return mapToDto(cartItemRepo.save(cartItem));
    }

    // Get Cart By UserId
    public List<CartItemDto> getCartById(Long userId){
        return cartItemRepo.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // Entity => DTO
    public CartItemDto mapToDto(CartItem cartItem){
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
