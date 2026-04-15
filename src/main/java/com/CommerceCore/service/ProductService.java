package com.CommerceCore.service;

import com.CommerceCore.dto.ProductDto;
import com.CommerceCore.entity.Category;
import com.CommerceCore.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    public ProductDto mapToDto(Product product){
        // Entity => DTO
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .categoryId(product.getCategory()!=null?product.getCategory().getId():null)
                .categoryName(product.getCategory()!=null?product.getCategory().getName():null)
                .build();
    }

    // DTO => Entity
    public Product mapToEntity(ProductDto dto, Category category){
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(category)
                .build();
    }
}
