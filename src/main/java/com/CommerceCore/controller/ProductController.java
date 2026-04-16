package com.CommerceCore.controller;

import com.CommerceCore.dto.ProductDto;
import com.CommerceCore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto dto){
        return productService.createProduct(dto);
    }

    @GetMapping
    public List<ProductDto> getAllProduct(){
        return productService.getAllProduct();
    }

    @GetMapping("/{id}")
    public ProductDto geProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }
}
