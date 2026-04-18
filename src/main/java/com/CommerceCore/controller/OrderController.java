package com.CommerceCore.controller;

import com.CommerceCore.dto.OrderDto;
import com.CommerceCore.entity.CustomUserPrincipal;
import com.CommerceCore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@EnableMethodSecurity
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/place")
    public OrderDto createOrder(Authentication authentication){
        CustomUserPrincipal principal=(CustomUserPrincipal) authentication.getPrincipal();
        Long userId= principal.getId();
        return orderService.placeOrder(userId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<OrderDto> getUserOrder(Authentication authentication){
        CustomUserPrincipal principal=(CustomUserPrincipal) authentication.getPrincipal();
        Long userId= principal.getId();
        return orderService.getUserOrders(userId);
    }

}
