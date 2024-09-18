package com.parcialuno.backend.services;

import com.parcialuno.backend.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService
{
    List<Product> findAll();
    Optional<Product> findById(Integer id);
    Product save(Product product);
    Product update(Integer id, Product product);
    void deleteById(Integer id);
}