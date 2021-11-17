package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.NewsRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRatingRepository extends JpaRepository<NewsRating, Integer> {
}