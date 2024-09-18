package com.parcialuno.backend.repositories;

import com.parcialuno.backend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>
{

}