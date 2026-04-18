package com.CommerceCore.controller;

import com.CommerceCore.dto.ProductDto;
import com.CommerceCore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@EnableMethodSecurity
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto dto){
        return productService.createProduct(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public List<ProductDto> getAllProduct(){
        return productService.getAllProduct();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ProductDto geProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }
}
