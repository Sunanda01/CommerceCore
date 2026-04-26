package com.CommerceCore.controller;

import com.CommerceCore.dto.CartItemDto;
import com.CommerceCore.security.CustomUserPrincipal;
import com.CommerceCore.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@EnableMethodSecurity
public class CartController {
    private final CartItemService cartItemService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public CartItemDto addToCart(
            Authentication authentication,
            @RequestParam Long productId,
            @RequestParam int quantity
    ){
        CustomUserPrincipal principal=(CustomUserPrincipal) authentication.getPrincipal();
        Long userId=principal.getId();
        return cartItemService.addToCart(userId, productId, quantity);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<CartItemDto> getCartById(Authentication authentication){
        CustomUserPrincipal principal=(CustomUserPrincipal) authentication.getPrincipal();
        Long userId= principal.getId();
        return cartItemService.getCartById(userId);
    }
}
