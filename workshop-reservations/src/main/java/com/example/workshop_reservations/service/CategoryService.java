package com.example.workshop_reservations.service;

import com.example.workshop_reservations.dto.CategoryRequest;
import com.example.workshop_reservations.dto.CategoryResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.CategoryMapper;
import com.example.workshop_reservations.model.Category;
import com.example.workshop_reservations.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    // Create a new category
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        // optional business rule: unique category name
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Category name already exists: " + request.getName());
        }
        Category saved = categoryRepository.save(categoryMapper.toEntity(request));
        return categoryMapper.toResponse(saved);
    }

    // Get all categories
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    // Get category by id
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        return categoryMapper.toResponse(category);
    }

    // Update category
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

        // Prevent duplicate names
        if (!category.getName().equalsIgnoreCase(request.getName())
                && categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Category name already exists: " + request.getName());
        }

        categoryMapper.updateEntity(category, request);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    // Delete category
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }
}