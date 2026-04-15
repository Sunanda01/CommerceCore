package com.CommerceCore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private Double price;

    private Long productId;
    private String productName;
}
