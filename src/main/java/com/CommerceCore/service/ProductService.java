package com.CommerceCore.service;

import com.CommerceCore.dto.ProductDto;
import com.CommerceCore.entity.Category;
import com.CommerceCore.entity.Product;
import com.CommerceCore.repository.CategoryRepo;
import com.CommerceCore.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    // Create Product
    public ProductDto createProduct(ProductDto dto){
        Category category=categoryRepo.findById(dto.getCategoryId()).orElseThrow(()->new RuntimeException("Category Not Found"));
        Product product=mapToEntity(dto,category);
        Product saved=productRepo.save(product);
        return mapToDto(saved);
    }


    // Get All Product
    public List<ProductDto> getAllProduct(){
        return productRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // Get Product By ID
    public ProductDto getProductById(Long productId){
        Product product=productRepo.findById(productId)
                .orElseThrow(()->new RuntimeException("Product Not Found"));
        return mapToDto(product);
    }

    // Entity => DTO
    public ProductDto mapToDto(Product product){
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
