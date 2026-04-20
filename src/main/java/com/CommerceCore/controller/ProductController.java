package com.CommerceCore.controller;

import com.CommerceCore.dto.ProductDto;
import com.CommerceCore.entity.PageResponse;
import com.CommerceCore.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
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
    public ProductDto createProduct(@Valid @RequestBody ProductDto dto){
        return productService.createProduct(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public PageResponse<ProductDto> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        return productService.getAllProduct(page, size, sortBy, direction);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/filter")
    public PageResponse<ProductDto> getFilterProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,

            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ){
        return productService.getFilterProduct(page, size, sortBy, direction, keyword, category, minPrice, maxPrice);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/specification")
    public PageResponse<ProductDto> getFilterProductSpecification(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,

            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ){
        return productService.getFilterProductSpecification(page, size, sortBy, direction, keyword, category, minPrice, maxPrice);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ProductDto geProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }
}
