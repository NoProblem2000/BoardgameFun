package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.GameCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameCategoryRepository extends JpaRepository<GameCategory, Integer> {
}