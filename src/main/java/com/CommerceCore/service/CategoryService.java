package com.CommerceCore.service;

import com.CommerceCore.dto.CategoryDto;
import com.CommerceCore.dto.OrderDto;
import com.CommerceCore.entity.Category;
import com.CommerceCore.entity.Order;
import com.CommerceCore.entity.OrderStatus;
import com.CommerceCore.entity.User;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    public CategoryDto mapToDto(Category category){
        // Entity => DTO
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    // DTO => Entity
    public Category mapToEntity(CategoryDto dto){
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
