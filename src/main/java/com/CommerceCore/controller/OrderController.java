package com.CommerceCore.controller;

import com.CommerceCore.dto.OrderDto;
import com.CommerceCore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/place")
    public OrderDto createOrder(@RequestParam Long userId){
        return orderService.placeOrder(userId);
    }

    @GetMapping
    public List<OrderDto> getUserOrder(@RequestParam Long userId){
        return orderService.getUserOrders(userId);
    }

}
