package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}