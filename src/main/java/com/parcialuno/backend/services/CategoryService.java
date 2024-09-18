package com.parcialuno.backend.services;

import com.parcialuno.backend.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService
{
    List<Category> findAll();
    Optional<Category> findById(Integer id);
    Category save(Category category);
    Category update(Integer id, Category category);
    void deleteById(Integer id);
}