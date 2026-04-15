package com.CommerceCore.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;

    // Flatten Category
    private Long categoryId;
    private String categoryName;
}
