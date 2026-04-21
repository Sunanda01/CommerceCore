package com.CommerceCore.service;

import com.CommerceCore.dto.OrderDto;

import com.CommerceCore.dto.OrderItemDto;
import com.CommerceCore.entity.*;
import com.CommerceCore.exception.ApiException;
import com.CommerceCore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;
    private final OrderItemRepo orderItemRepo;
    private final OrderItemService itemService;
    private final ProductRepo productRepo;

    // Place order
    @CacheEvict(value = {"products", "productFilter", "productSpecification"}, allEntries = true)
    @Transactional
    public OrderDto placeOrder(long userId){
        User user=userRepo.findById(userId).orElseThrow(()->new ApiException("User Not Found", HttpStatus.NOT_FOUND));
        // 1. Get Cart Items of User
        List<CartItem> cartItems=cartItemRepo.findByUserId(userId);
        if(cartItems.isEmpty()){
            throw new ApiException("Cart is Empty",HttpStatus.BAD_REQUEST);
        }

        // 2. Create order without total amount
        Order order=Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();
        Order savedOrder=orderRepo.save(order);

        double totAmount=0;
        List<OrderItem> orderItems = new ArrayList<>();
        // 3. Convert Cart → OrderItems
        for (CartItem cartItem:cartItems){
            Product product=cartItem.getProduct();
            int quantity=cartItem.getQuantity();

            if(product.getStock()<quantity){
                throw new ApiException("Product "+product.getName()+" has only "+product.getStock()+" stock left",HttpStatus.BAD_REQUEST);
            }

            product.setStock(product.getStock()-quantity);
            productRepo.save(product);

            double price=product.getPrice();
            OrderItem orderItem=OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(quantity)
                    .price(price)
                    .build();
            totAmount+=price*quantity;
            orderItems.add(orderItem);  // Each item stored separately
        }
        orderItemRepo.saveAll(orderItems);
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
        List<OrderItemDto> items=orderItemRepo.findByOrderId(order.getId())
                .stream()
                .map(itemService::mapToDto)
                .toList();

        return OrderDto.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus().name())  // enum => string
                .items(items)
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
