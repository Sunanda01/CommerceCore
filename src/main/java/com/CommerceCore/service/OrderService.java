package com.CommerceCore.service;

import com.CommerceCore.dto.CartItemDto;
import com.CommerceCore.dto.OrderDto;
import com.CommerceCore.dto.ProductDto;
import com.CommerceCore.entity.*;
import com.CommerceCore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;
    private final OrderItemRepo orderItemRepo;

    // Place order
    @Transactional
    public OrderDto placeOrder(long userId){
        User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));
        // 1. Get Cart Items of User
        List<CartItem> cartItems=cartItemRepo.findByUserId(userId);
        if(cartItems.isEmpty()){
            throw new RuntimeException("cart is Empty");
        }

        // 2. Create order without total amount
        Order order=Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();
        Order savedOrder=orderRepo.save(order);

        double totAmount=0;
        // 3. Convert Cart → OrderItems
        for (CartItem cartItem:cartItems){
            double price=cartItem.getProduct().getPrice();
            int quantity=cartItem.getQuantity();
            OrderItem orderItem=OrderItem.builder()
                    .order(savedOrder)
                    .product(cartItem.getProduct())
                    .quantity(quantity)
                    .price(price)
                    .build();
            totAmount+=price*quantity;
            orderItemRepo.save(orderItem);  // Each item stored separately
        }
        savedOrder.setTotalAmount(totAmount);
        orderRepo.save(savedOrder);         // Update Total in Order
        cartItemRepo.deleteAll(cartItems);  // After order cart gets empty
        return mapToDto(savedOrder);
    }

    // Get User orders
    public List<OrderDto> getUserOrders(Long userId){
        return orderRepo.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // Entity => DTO
    public OrderDto mapToDto(Order order){
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
