package com.CommerceCore.service;

import com.CommerceCore.dto.ProductDto;
import com.CommerceCore.entity.Category;
import com.CommerceCore.entity.PageResponse;
import com.CommerceCore.entity.Product;
import com.CommerceCore.exception.ApiException;
import com.CommerceCore.repository.CategoryRepo;
import com.CommerceCore.repository.ProductRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    // Create Product
    public ProductDto createProduct(ProductDto dto){
        Category category=categoryRepo.findById(dto.getCategoryId()).orElseThrow(()->new ApiException("Category Not Found", HttpStatus.NOT_FOUND));
        Product product=mapToEntity(dto,category);
        Product saved=productRepo.save(product);
        return mapToDto(saved);
    }

    // Get All Product
    public PageResponse<ProductDto> getAllProduct(int page, int size, String sortBy, String direction){
        Sort sort=direction.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Product> productPage=productRepo.findAll(pageable);
        return PageResponse.<ProductDto> builder()
                .content(productPage.getContent().stream().map(this::mapToDto).toList())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
    }

    // Get Product By ID
    public ProductDto getProductById(Long productId){
        Product product=productRepo.findById(productId)
                .orElseThrow(()->new ApiException("Product Not Found",HttpStatus.NOT_FOUND));
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
