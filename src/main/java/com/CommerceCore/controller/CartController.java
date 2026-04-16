package com.CommerceCore.controller;

import com.CommerceCore.dto.CartItemDto;
import com.CommerceCore.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartItemService cartItemService;

    @PostMapping("/add")
    public CartItemDto addToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ){
        return cartItemService.addToCart(userId, productId, quantity);
    }

    @GetMapping
    public List<CartItemDto> getCartById(@RequestParam Long userId){
        return cartItemService.getCartById(userId);
    }
}
