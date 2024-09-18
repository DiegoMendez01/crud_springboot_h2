package com.parcialuno.backend.controllers;

import com.parcialuno.backend.dto.CategoryDTO;
import com.parcialuno.backend.dto.ProductDTO;
import com.parcialuno.backend.mappers.CategoryMapper;
import com.parcialuno.backend.mappers.ProductMapper;
import com.parcialuno.backend.models.Category;
import com.parcialuno.backend.models.Product;
import com.parcialuno.backend.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController
{
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryMapper::toDto)
                .toList();
        return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Integer id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(value -> new ResponseEntity<>(categoryMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.save(categoryMapper.toEntity(categoryDTO));
        return ResponseEntity.ok(categoryMapper.toDto(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        category.setId(id);
        Category updatedCategory = categoryService.update(id, category);
        if (updatedCategory != null) {
            return new ResponseEntity<>(categoryMapper.toDto(updatedCategory), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCategory(@RequestParam Integer id) {
        categoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Integer id) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            // Convierte la lista de productos a DTOs
            List<ProductDTO> productDTOs = category.getProducts().stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(productDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}