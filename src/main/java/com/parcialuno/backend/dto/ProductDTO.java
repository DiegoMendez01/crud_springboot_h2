package com.parcialuno.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO
{
    private Integer id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private Double price;

    @NotNull(message = "La categoria es obligatorio")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer categoryId; // Solo ID de la categoría

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}