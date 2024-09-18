package com.parcialuno.backend.mappers;

import com.parcialuno.backend.dto.CategoryDTO;
import com.parcialuno.backend.dto.ProductDTO;
import com.parcialuno.backend.models.Product;
import com.parcialuno.backend.models.Category;
import com.parcialuno.backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductMapper {

    @Autowired
    private CategoryService categoryService;

    // Convertir de Product a ProductDTO
    public ProductDTO toDto(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory() != null ? product.getCategory().getId() : null, // Solo ID de la categoría
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    // Convertir de ProductDTO a Product
    public Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());

        if (productDTO.getCategoryId() != null) {
            Optional<Category> categoryOptional = categoryService.findById(productDTO.getCategoryId());
            if (categoryOptional.isPresent()) {
                product.setCategory(categoryOptional.get());
            } else {
                product.setCategory(null); // Manejo de categoría no encontrada
            }
        } else {
            product.setCategory(null);
        }

        return product;
    }
}