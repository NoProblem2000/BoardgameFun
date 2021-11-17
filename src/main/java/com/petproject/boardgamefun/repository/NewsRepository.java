package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Integer> {
}