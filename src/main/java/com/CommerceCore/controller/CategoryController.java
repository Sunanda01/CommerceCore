package com.CommerceCore.controller;

import com.CommerceCore.dto.CategoryDto;
import com.CommerceCore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryDto dto){
        return categoryService.createCategory(dto);
    }

    @GetMapping
    public List<CategoryDto> getAllCategory(){
        return categoryService.getAllCategory();
    }
}
