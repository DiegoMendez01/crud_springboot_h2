package com.parcialuno.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO
{
    private Integer id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String name;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Integer> productIds; // Solo IDs de productos

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}