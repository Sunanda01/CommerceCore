package com.CommerceCore.service;

import com.CommerceCore.dto.ProductDto;
import com.CommerceCore.entity.Category;
import com.CommerceCore.entity.PageResponse;
import com.CommerceCore.entity.Product;
import com.CommerceCore.entity.ProductSpecification;
import com.CommerceCore.exception.ApiException;
import com.CommerceCore.repository.CategoryRepo;
import com.CommerceCore.repository.ProductRepo;
import com.CommerceCore.repository.ProductRepoSpecification;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ProductRepoSpecification repoSpecification;

    // Create Product
    @CacheEvict(value = {"products", "productFilter", "productSpecification"}, allEntries = true)
    public ProductDto createProduct(ProductDto dto){
        Category category=categoryRepo.findById(dto.getCategoryId()).orElseThrow(()->new ApiException("Category Not Found", HttpStatus.NOT_FOUND));
        Product product=mapToEntity(dto,category);
        Product saved=productRepo.save(product);
        return mapToDto(saved);
    }

    // Get All Product
    // http://localhost:8080/api/products?page=1&size=3&sortBy=price&direction=asc
    @Cacheable(value = "products", key = "#page + '-' + #size + '-' + #sortBy + '-' + #direction")
    public PageResponse<ProductDto> getAllProduct(int page, int size, String sortBy, String direction){
        System.out.println("DB HIT");
        Sort sort=direction.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
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

    // Get Filtered Product + Pagination
    // 1. http://localhost:8080/api/products/filter?category=Mobile&sortBy=price&direction=asc
    // 2. http://localhost:8080/api/products/filter?name=Vivo&category=Mobile&sortBy=price&direction=asc
    @Cacheable(value = "productFilter", key = "#page + '-' + #size + '-' + #sortBy + '-' + #direction + '-' + #keyword + '-' + #category + '-' + #minPrice + '-' + #maxPrice ")
    public PageResponse<ProductDto> getFilterProduct(
            int page,
            int size,
            String sortBy,
            String direction,

            String keyword,
            String category,
            Double minPrice,
            Double maxPrice
    ){
        System.out.println("DB HIT FILTER");
        Sort.Direction dir=Sort.Direction.fromString(direction);
        Pageable pageable= PageRequest.of(page, size, Sort.by(dir,sortBy));
        Page<Product> productPage=productRepo.filterProducts(keyword, category, minPrice, maxPrice, pageable);
        return PageResponse.<ProductDto> builder()
                .content(productPage.getContent().stream().map(this::mapToDto).toList())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
    }

    // Get Filtered Product Using Specification + Pagination
    // http://localhost:8080/api/products/specification?category=Mobile&sortBy=price&direction=desc
    @Cacheable(value = "productSpecification", key = "#page + '-' + #size + '-' + #sortBy + '-' + #direction + '-' + #keyword + '-' + #category + '-' + #minPrice + '-' + #maxPrice ")
    public PageResponse<ProductDto> getFilterProductSpecification(
            int page,
            int size,
            String sortBy,
            String direction,

            String keyword,
            String category,
            Double minPrice,
            Double maxPrice
    ){
        System.out.println("DB HIT FILTER SPECIFICATION");
        Sort.Direction dir=Sort.Direction.fromString(direction);
        Pageable pageable= PageRequest.of(page, size, Sort.by(dir,sortBy));
        Specification<Product> spec = ProductSpecification.filterProduct(
                keyword, category, minPrice, maxPrice
        );
        Page<Product> productPage=repoSpecification.findAll(spec, pageable);
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
