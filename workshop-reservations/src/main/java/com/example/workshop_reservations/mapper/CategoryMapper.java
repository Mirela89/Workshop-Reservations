package com.example.workshop_reservations.mapper;

import com.example.workshop_reservations.dto.CategoryRequest;
import com.example.workshop_reservations.dto.CategoryResponse;
import com.example.workshop_reservations.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    // Convert CategoryRequest DTO to Category entity
    public Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .build();
    }

    // Update existing Category entity with data from CategoryRequest DTO
    public void updateEntity(Category category, CategoryRequest request) {
        category.setName(request.getName());
    }

    // Convert Category entity to CategoryResponse DTO
    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .build();
    }
}