package com.CommerceCore.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    @NotBlank(message = "Product name required")
    private String name;
    @NotNull(message = "Price required")
    @Positive(message = "Price must be positive")
    private Double price;
    private Integer stock;

    // Flatten Category
    private Long categoryId;
    private String categoryName;
}
