package com.parcialuno.backend.mappers;

import com.parcialuno.backend.dto.CategoryDTO;
import com.parcialuno.backend.dto.ProductDTO;
import com.parcialuno.backend.models.Category;
import com.parcialuno.backend.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    @Autowired
    private ProductMapper productMapper; // Inyecta ProductMapper

    // Convertir de Category a CategoryDTO
    public CategoryDTO toDto(Category category) {
        List<Integer> productIds = category.getProducts() != null ? category.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toList()) : null;

        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                productIds,
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    // Convertir de CategoryDTO a Category
    public Category toEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        // No se establece la lista de productos en el DTO ya que solo se usan los IDs
        return category;
    }
}